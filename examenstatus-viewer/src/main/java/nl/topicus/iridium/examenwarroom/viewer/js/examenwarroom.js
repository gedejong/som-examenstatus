$(function() {
	"use strict";
	warroomWorker.init();
});

var warroomWorker = {
	init: function(){
		drawTakenGrafiek();
		var numberMinutes = config.maxDuur / 60;
		$("#takenXmin").html(numberMinutes + " min");
	},
	updateNiveauLabel: function(){
			var adres = warroom.straten[0].url;
			$.ajax({
				url : adres + '/taken/niveau?token=' + authToken,
				type : 'GET',
				dataType : "text",
				error : function(request, error) {
					console.log(error);
				},
				success : function(niveauName) {
						$("#takenHeadLabelRight").html(niveauName.toUpperCase());
				}
			});
	}	

}


function fillDataSet(newDataset) {
	var taskIdList = [];
	var aantalObjecten = newDataset.length;
	for ( var i = 0; i < aantalObjecten; i++) {
		var obj = newDataset[i];
		if (obj != null) {
			obj.timeLastUpdate = new Date().getTime();
			var taskId = obj.taskId;
			var inArrayIds = $.inArray(taskId, warroom.indexTaakIdArray);
			if(inArrayIds === -1){
				// not in array, nieuwe taak
				obj.xCurrent = 0;
				obj.yCurrent = 0;
				var newLength = warroom.takenArray.push(obj);
				warroom.indexTaakIdArray.push(taskId);
			}else{
				// bestaande taak
				var indexArrayCrashteTaken = warroom.gecrashteTaken[taskId];

				if(obj.staat !== "Gestopt"){
					// als object eerder gecrasht is geweest
					var arrayHelper = arrayTools.getArrayHelper();
					var numberOfCrashes = arrayHelper.countNumberOfOccurencesById(taskId, warroom.straatCounterGecrashteTaken);
					if(numberOfCrashes >= 1){
						// dan doodskop plaatsen op die plek
						// var left = takenPanel.width;
						// var stringBuilder = tools.getStringBuilder();
						// stringBuilder.append("<div class='taskMessage' style='margin-left: '>");
						// stringBuilder.append(indexArrayCrashteTaken.school.naam);
						// stringBuilder.append("<br />");
						// stringBuilder.append(indexArrayCrashteTaken.timeLastUpdate);
						// stringBuilder.append("<br />");
						// stringBuilder.append("straat ");
						// stringBuilder.append(indexArrayCrashteTaken.straat.straatId);
						// stringBuilder.append("<i class='iconewr iconewr-dead'></i>");
						// stringBuilder.append("</div>");
						// $("#takenSVGposition").append(stringBuilder.toString());
						// // taak verwijderen uit lijst met gecrashte taken
						// warroom.gecrashteTaken[taskId] = null;
					}
					// in array
					warroom.takenArray[inArrayIds] = obj;
				}else{
					// gestopt en bestaand
					var oldObj = warroom.takenArray[inArrayIds];
					oldObj.staat = "Gestopt";
					warroom.takenArray[inArrayIds] = oldObj;
					if(indexArrayCrashteTaken === null || indexArrayCrashteTaken === undefined){
						warroom.gecrashteTaken[oldObj.taskId] = oldObj;
					}

					var crashObj = {
						taskId : oldObj.taskId,
						straatId : oldObj.straat.straatId
					}
					if(arrayTools.getArrayHelper().countNumberOfOccurences(crashObj, warroom.straatCounterGecrashteTaken) === 0){
						warroom.straatCounterGecrashteTaken.push(crashObj);
					}
				}

				processIntoLongDuringTasksIfNeeded(obj);
			}
			taskIdList.push(taskId);
		}
	}

	// set progressie = 110 voor taken die klaar zijn
	for ( var index in warroom.takenArray) {
		if (typeof index != "undefined") {
			var object = warroom.takenArray[index];
			index = parseInt(index);
			inArray = $.inArray(object.taskId, taskIdList);
			var inArrayIds2 = $.inArray(object.taskId, warroom.indexTaakIdArray);
			if (inArray === -1 && inArrayIds2 !== -1) {
				// omdat taken nooit progressie 100% hebben
				if (object.progressie <= 100 && object.staat !== "Gestopt") {
					object.progressie = 110;
					warroom.takenArray[index] = object;
				}
			}
		}
	}
	
	/**	
	*	taak duur > max duur grafiek
	*		taak id ophalen	
	*		taak in array met lang durende taken zetten
	*		taakid in index array zetten
	*		return true
	*	return false
	*/
	function processIntoLongDuringTasksIfNeeded(taak){
		// taak duur > max duur grafiek
		// var r = new drawTakenGrafiek().berekenR(taak);
		// var x = new drawTakenGrafiek().berekenX(taak, r);
		var x = 0;
		if(x > config.maxDuur){
			takenPanel.teLangDurendeTaken.push(taak);
			takenPanel.telangDurendeTakenIndex.push(taak.taskId);
			return true;
		}
		return false;
	}
}



/** General functions */

/* draw gridlines */
function drawGridLines(numberHorizontalGridLines, height, width, svg) {	
	// Gevarengebied
	var partOfWidthAtPointZero = (config.maximumDuur0procent / config.maxDuur);
	var restOfWidth =  (takenPanel.width-60) * partOfWidthAtPointZero;
	var partPercentageHeight = config.maximumPercentageGevarengebiedBijMaximumTijdsduur / 100;
	var heightOfEnd = takenPanel.height - ( partPercentageHeight * takenPanel.height);
	// draw lines
	for(var counter = restOfWidth; counter < takenPanel.width - 60; counter += 12){
		svg.append("svg:line").attr("x1", counter).attr("x2", counter).attr("y1", heightOfEnd)
		.attr("y2", takenPanel.height).attr("stroke", "#d9d9d9");
	}
	for(var counterHeight = heightOfEnd; counterHeight < takenPanel.height; counterHeight += 12){
		svg.append("svg:line").attr("x1", restOfWidth).attr("x2", takenPanel.width - 60).attr("y1", counterHeight)
		.attr("y2", counterHeight).attr("stroke", "#d9d9d9");
	}

	var cx = (takenPanel.width-60) / 2;
	// var cy = takenPanel.height / 2;
	var cy = takenPanel.height - ( partPercentageHeight * takenPanel.height );
	if(partOfWidthAtPointZero <= 0.5){
		var rx = takenPanel.width - restOfWidth;
		cx = restOfWidth;
	}else{
		var rx = takenPanel.width / 2;
	}
	// if(partPercentageHeight)
	
	var ry = (partPercentageHeight * takenPanel.height);
	// draw polygon for curve. White background
	svg.append("svg:ellipse").attr("cx", cx )
	.attr("cy", cy)
	.attr("rx", rx)
	.attr("ry", ry)
	.style("fill", "#ffffff");
}
