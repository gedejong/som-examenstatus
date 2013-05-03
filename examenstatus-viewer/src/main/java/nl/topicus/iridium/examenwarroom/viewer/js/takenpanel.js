"use strict";
var takenPanel = {
	/** settings */
	width : 1080,
	height : 480,
	refreshRateSeconds : 1,
	durationTransition : 1200, // ms

	maxDurationBeforeOrange : config.naarOranje * 1000, // ms
	maxDurationBeforeRed : config.naarRood * 1000, // ms
	// dots
	maxRadius : 12,
	minRadius : 4,
	staticRadiusToLong: 16,
	// gridlines
	numberHorizontalGridLines : 10,
	gridLineHeight : 1,

	// declare variables
	svg : null,
	svgProgress: null,
	maxAantalExamenLeerling : 0,
	minAantalExamenLeerling : Number.MAX_VALUE,
	jsonObject : warroom.takenArray,
	minBeginTijd : Number.MAX_VALUE,
	maxBeginTijd : 0,
	maxDuur: config.maxDuur * 1000, // ms
	aantalTaken : 0,
	tooltipDiv : d3.selectAll("div#takenTooltip"),
	teLangDurendeTaken : [],
	teLangDurendeTakenIndex: [],
	taakStatusAantallen : {
		nogTeDoen: 0,
		inQueue: 0,
		lopend: 0, 
		gestopt: 0,
		klaar: 0
	},
	taakStatusAantallenDone : {
		nogTeDoen : 0,
		inQueue : 0,
		lopend: 0, 
		gestopt: 0,
		klaar: 0
	},
	gestopteTakenId: []
};

$("#takenSVGposition").css("min-height", takenPanel.height);

function drawTakenGrafiek() {
	$('.carousel').carousel();
	
	
	if (takenPanel.svg === null) {
		takenPanel.svg = d3.select("div#takenSVGposition").append("svg").attr(
			"width", takenPanel.width).attr("height", takenPanel.height)
		.attr('id', "takenSVG");
		drawGridLines(takenPanel.numberHorizontalGridLines, takenPanel.height,
			takenPanel.width, takenPanel.svg);
	}
	if(takenPanel.svgProgress === null){
		takenPanel.svgProgress = d3.select("div#svg_progress_area").append('svg').attr('width', 1040).attr('height', 89).attr('id', 'progressSvg');
	}

	function updateProgressLabels(nogTeDoen, inQueue, lopend, gestopt, klaar){
		var totaal = nogTeDoen + inQueue + lopend + gestopt + klaar;
		$("#bucket_label_nog_te_doen").html(nogTeDoen + " | " + totaal);
		$("#bucket_label_in_queue").html(inQueue + " | " + totaal);
		$("#bucket_label_lopend").html(lopend + " | " + totaal);
		$("#bucket_label_gestopt").html(gestopt + " | " + totaal);
		$("#bucket_label_klaar").html(klaar + " | " + totaal);
	}
	function berekenY(d, r) {
		var progressie = d.progressie;
		return takenPanel.height - (progressie * (takenPanel.height / 100));
	}
	function berekenX(d, r) {
		var duur = d.timeLastUpdate - d.beginTijd;
		var tijdspercentage = (duur / takenPanel.maxDuur) * 100;
		var hoogteStip = (tijdspercentage * (takenPanel.width / 100) + 10);
		var berekendeBreedte = hoogteStip;
		if (berekendeBreedte >= takenPanel.width) {
			berekendeBreedte = takenPanel.width - r;
		}
		return berekendeBreedte;
	}

	function bepaalKleur(d) {
		// console.log(d);
		if(d.staat == "Lopend"){
			var duur = d.timeLastUpdate - d.beginTijd;
			if (duur > takenPanel.maxDurationBeforeRed)
				return "redDots";
			if (duur > takenPanel.maxDurationBeforeOrange)
				return "orangeDots"
		}else{
			var taakId = d.taskId;
			var inArrayDefecteTaken = $.inArray(taakId, takenPanel.gestopteTakenId);
			if(inArrayDefecteTaken === -1){
				takenPanel.gestopteTakenId.push(taakId);
				$("#notification-message-holder").append("<div class='item'><i class='iconewr iconewr-notification'></i>" + d.naam + " taak gestopt</div>");
				if($("#notificationArea").css('opacity') == 0){
					$("#notificationArea").animate({'opacity': '1.0'}, {duration: 2000, easing: 'swing'});
				}
			}
			return "redDots";
		}
		return "greyDots";
	}

	function berekenR(d) {
		var percentageExamenLeerlingenVanGeheel = (d.school.aantalExamenLeerlingen / takenPanel.maxAantalExamenLeerling) * 100;
		var radius = takenPanel.minRadius + percentageExamenLeerlingenVanGeheel
		* (takenPanel.maxRadius / 100);
		if(radius < takenPanel.maxRadius)
			return radius;
		else
			return takenPanel.maxRadius;
	}
	
	function processUpdatingProgressDots(){
		setInterval(function() {
			var aantalStraten = warroom.straten.length;
			for ( var i = 0; i < aantalStraten; i++) {
				var adres = warroom.straten[i].url;
				var straatId = warroom.straten[i].id;
				$.ajax({
					url : adres + '/taken/aantallen/perstate?token=' + authToken,
					type : 'GET',
					dataType : "json",
					error : function(request, error) {
						console.log("error" + request + ", " + error);
					},
					success : function(newData, textStatus) {
						updateProgressDots(newData);
					}
				});
			}
		}, 1000);
	}

	function calculateTijden() {
		takenPanel.maxBeginTijd = 0;
		takenPanel.minBeginTijd = Number.MAX_VALUE;

		var aantalTaken = 0;
		if (takenPanel.jsonObject !== null){
			aantalTaken = takenPanel.jsonObject.length;
		}

		for ( var taakCounter = 0; taakCounter < aantalTaken; taakCounter++) {
			var obj = takenPanel.jsonObject[taakCounter];
			if (obj != null && obj.progressie <= 100) {
				if (obj.beginTijd > takenPanel.maxBeginTijd)
					takenPanel.maxBeginTijd = obj.beginTijd;
				if (obj.beginTijd < takenPanel.minBeginTijd)
					takenPanel.minBeginTijd = obj.beginTijd;
				if (obj.school.aantalExamenLeerlingen > takenPanel.maxAantalExamenLeerling)
					takenPanel.maxAantalExamenLeerling = obj.school.aantalExamenLeerlingen;
				if (obj.school.aantalExamenLeerlingen < takenPanel.minAantalExamenLeerling)
					takenPanel.minAantalExamenLeerling = obj.school.aantalExamenLeerlingen;
			}

		}
		// print some logging information
		// printLogging();
		function printLogging() {
			console.log("current timestamp: " + currentTimeStamp);
			console.log("aantal taken: " + aantalTaken);
			console.log("min begintijd: " + minBeginTijd);
			console.log("max begintijd: " + maxBeginTijd);
			console.log("max duur: " + maxDuur);
		}
	}
	calculateTijden();

	function redraw() {
		// fillDataSet(newData);
		calculateTijden();

		var groepen = takenPanel.svg.selectAll("g").data(warroom.takenArray);
		// update groepen
		groepen.transition().duration(takenPanel.durationTransition).ease('linear').attr('transform', function(d, i){
			var r = berekenR(d);
			var xNew = berekenX(d, r);
			var yNew = berekenY(d, r);
			// if(xNew >= takenPanel.width - 25){
			// 	xNew = takenPanel.width - 25;
			// }
			return "translate(" + xNew + "," + yNew + ")";
		}).select("circle.mainDot").attr('class', function(d){
			var r = berekenR(d);
			var xNew = berekenX(d, r);
			// if(xNew >= takenPanel.width - 25){
			// 	return "whiteDot";
			// }else{
				return bepaalKleur(d);
			// }
		}).attr("r", function(d){
			var r = berekenR(d);
			// var xNew = berekenX(d, r);
			// if(xNew >= takenPanel.width - 25){
			// 	return takenPanel.staticRadiusToLong;
			// }else{
				return r;
			// }
		})

		groepen.select("circle.alertDot").transition().duration(takenPanel.durationTransition).attr("class", function(d){
			var arrayHelper = arrayTools.getArrayHelper();
			var numberCrashes = arrayHelper.countNumberOfCrashes(d.straat.straatId, warroom.straatCounterGecrashteTaken);
			if(numberCrashes >= 2){
				return "alertDot visibleDot";
			}
			return "alertDot notVisible";
		});
		groepen.select("text").transition().duration(takenPanel.durationTransition).attr("class", function(d){
			var arrayHelper = arrayTools.getArrayHelper();
			var numberCrashes = arrayHelper.countNumberOfCrashes(d.straat.straatId, warroom.straatCounterGecrashteTaken);
			if(numberCrashes >= 2){
				return "visibleDot";
			}
			return "notVisible";
		});

		groepen.select("text.textLabelDurationToLong").transition()
		.duration(takenPanel.durationTransition)
		.attr('class', function(d){
			var r = berekenR(d);
			var xNew = berekenX(d, r);
			if(xNew >= takenPanel.width - 25){ // to long
				return "visibleLabelDurationToLong";
			}else{
				return "textLabelDurationToLong";
			}
		});


		// insert new items
		var dta = groepen.enter();
		var groupje = dta.append("g").attr('transform', function(d, i){
			return "translate(0," + takenPanel.height + ")";
		});
		// add timer circle
		// groupje.append("circle").attr("class", "timerCircle").attr("r", takenPanel.staticRadiusToLong + 3 )
		// .attr("cx", 0).attr("cy", 0);

		// normal circle
		groupje.append("circle").attr("cx", function(d){
			var r = d3.select(this).attr("r");
			return 0;
		}).attr("cy", function(d){
			var r = d3.select(this).attr("r");
			return 0;
		}).attr("class", function(d) {
			return "mainDot " + bepaalKleur(d);
		}).attr("r", function(d){
			return berekenR(d);
		});

		groupje.append("circle").attr("cx", function(d){
			// bereken R van parent
			var r = berekenR(d);
			return r;
		}).attr("cy", function(d){
			// bereken R van parent
			var r = berekenR(d);
			return -r;
		}).attr("class", "alertDot notVisible").attr("r", 7);
		groupje.append("text").text(function(d){
			return d.straat.straatId;
		}).attr({
			"font-size" : "10px",
			"alignment-baseline" : "middle",
			"text-anchor": "right"
		}).attr("y", function(d){
			var r = berekenR(d);
			return -r + 2;
		}).attr("x", function(d){
			var r = berekenR(d);
			return r - 3;
		}).attr("class", "notVisible");
		// add text label

		// groupje.append("text").text(function (d){
		// 	return d.school.naam
		// }).attr("x", function(d){
		// 	return (20 + d.school.naam.length * 6) * -1;
		// }).attr("y", function(d){
		// 	return -12;
		// }).attr("class", "textLabelDurationToLong");

		// groupje.append("text").text(function (d){
		// 	return d.formattedBeginTijd.substring(6, 16);
		// }).attr("x", function(d){
		// 	return (20 + d.school.naam.length * 6) * -1;
		// }).attr("y", function(d){
		// 	return 0;
		// }).attr("class", "textLabelDurationToLong");

		// groupje.append("text").text(function (d){
		// 	return "straat " + d.straat.straatId;
		// }).attr("x", function(d){
		// 	return (20 + d.school.naam.length * 6) * -1;
		// }).attr("y", function(d){
		// 	return 12;
		// }).attr("class", "textLabelDurationToLong");
		// // add pointer icon
		// groupje.append("svg:image").attr("xlink:href", "imgs/pointer.png")
		// .attr("width", 13).attr("height", 23)
		// .attr("x", -40).attr("y", -12);

		// // add timer label
		// groupje.append("text").text(function(d){
		// 	var duration = (d.timeLastUpdate - d.beginTijd) / (60*1000);
		// 	var durationMin = Math.floor(duration);
		// 	var seconds = (d.timeLastUpdate - d.begintijd) % 60;
		// 	return durationMin;
		// }).attr("class", "durationMinutesLabel")
		// .attr("x", -8).attr("y", 6);
		
		groepen.exit().remove();
	}
	
	function drawProgressIndicator(data){
		var circlesNogtedoen = takenPanel.svgProgress.selectAll("circle").data(data);
		// update circles
		circlesNogtedoen.transition().ease("linear").duration(function(d, i){
			if(d.style != null){
				return 0;
			}
			if(d.toTop){
				return 700;
			}else{
				return 800;
			}
		}).attr("cx", function(d, i){
			return d.x;
		}).attr('cy', function(d, i){
			return d.y;
		}).attr('fill-opacity', function(d){
			if(d.style != null){
				return 0;
			}else{
				return 1;
			}
		}).attr('class', function(d){
			return d.classname;
		});
		// add new circles
		circlesNogtedoen.enter().append("circle").attr("cx", function(d, i) {
			return d.x;
		}).attr("r", function(d) {
			return 3;
		}).attr("cy", function(d, i) {
			return d.y;
		}).attr("class", function(d) {
			return "greyDots";
		}).attr('class', function(d){
			return d.classname;
		});
		// delete circles
		circlesNogtedoen.exit().remove();
	}
	function updateProgressDots(jsonAantallen){
		var arrayTaken = [];
		var aantalNogTeStarten = jsonAantallen.NogTeDoen;		
		var aantallenTaken = [];
		aantallenTaken.push({
			xOffset : 0,
			aantalTaken: jsonAantallen.NogTeDoen,
			type: 'Nog te doen'
		});
		aantallenTaken.push({
			xOffset : 280,
			aantalTaken: jsonAantallen.InQueue,
			type: 'In wachtrij'
		});
		aantallenTaken.push({
			xOffset : 560,
			aantalTaken: jsonAantallen.Lopend,
			type: 'Lopend'
		});
		aantallenTaken.push({
			xOffset : 560, // don't show this one in the progress indicator
			aantalTaken: jsonAantallen.Gestopt,
			type: 'Gestopt'
		});
		aantallenTaken.push({
			xOffset : 840,
			aantalTaken: jsonAantallen.Klaar,
			type: 'Klaar'
		});
		var xValue = 1;
		var yCounter = 1;
		var aantalTakenPerKolom = 8;
		aantallenTaken.forEach(function(element, index, array){
			var aantalKolommen = (element.aantalTaken / (aantalTakenPerKolom - 1) )
			xValue = 1;
			yCounter = 1;
			for(var i = 0; i < element.aantalTaken; i++){
				if(yCounter == aantalTakenPerKolom){
					yCounter = 1;
					xValue++;
				}
				var obj = {
					x : xValue * aantalTakenPerKolom + element.xOffset,
					y: yCounter * aantalTakenPerKolom,
					style: null,
					toTop: false
				};
				// lopende taak moet grijs zijn
				if(element.type === "Gestopt"){
					obj.style = 'opacity';
					obj.classname = "blackDots";
				}else if(element.type === "Lopend"){
					obj.classname = "greyDots";
				}else{
					obj.classname = "blackDots";
				}

				if(xValue > 25){
					obj.style = 'opacity';
					obj.x = 25 * aantalTakenPerKolom + element.xOffset;
				}
				
				if(xValue % 2 == 0){
					obj.y = ( (aantalTakenPerKolom * aantalTakenPerKolom) - obj.y);
					obj.toTop = true;
				}
				
				yCounter++;
				arrayTaken.push(obj);
			}
		});
		drawProgressIndicator(arrayTaken);
		updateProgressLabels(jsonAantallen.NogTeDoen, jsonAantallen.InQueue, jsonAantallen.Lopend, jsonAantallen.Gestopt, jsonAantallen.Klaar);
	}
	processUpdatingProgressDots();
	warroomWorker.updateNiveauLabel();
	// refresh
	setInterval(function() {
		var aantalStraten = warroom.straten.length;
		for ( var i = 0; i < aantalStraten; i++) {
			var adres = warroom.straten[i].url;
			var straatId = warroom.straten[i].id;
			$.ajax({
				url : adres + '/taken/'+ straatId +'?token=' + authToken,
				type : 'GET',
				dataType : "json",
				error : function(request, error) {
					console.log(error);
				},
				success : function(newData) {
					fillDataSet(newData);

				}
			});
		}
		redraw();
		warroomWorker.updateNiveauLabel();
	}, takenPanel.refreshRateSeconds * 1000);

}

