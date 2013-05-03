var tools = {
	getStringBuilder: function(){
		var data =[];
		var counter = 0;
		return {
			append: function(s){ data[counter++] = s; return this; },
			remove: function(i, j){ data.splice(i, j || 1); return this; },
			inset: function(i, s){ data.splice(i, 0, s); return this; },
			toString: function(s){ return data.join(s || ""); return this}
		};
	}
}
var arrayTools = {
	getArrayHelper: function(){
		return{
			countNumberOfOccurences: function(needle, haystack){
				var numberOfOcc = 0;
				haystack.forEach(function(element, index, array){
					if(element.taskId === needle.taskId && element.straatId === needle.straatId){
						numberOfOcc++;
					}
				});
				return numberOfOcc;
			},
			countNumberOfCrashes: function(straatId, haystack){
				var number = 0;
				haystack.forEach(function(element, index, array){
					if(element.straatId === straatId)
						number++;
				});
				return number;
			}, 
			countNumberOfOccurencesById: function(needle, haystack){
			var numberOfOcc = 0;
				haystack.forEach(function(element, index, array){

					if(element.taskId === needle.taskId){
						numberOfOcc++;
					}
				});
				return numberOfOcc;	
			}
		};
	}
}