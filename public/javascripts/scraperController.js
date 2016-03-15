'use strict';
(function(){
    angular.module('scraperApp',[])
        .controller('ScraperCtrl',['$http','$scope',function($http,$scope){ 
    	$scope.scrape = function (){
    		$http({
    		    url: 'title', 
    		    method: "GET",
    		    params: {'url': $scope.url}
    		 })
        .success(function(response){ 
        	if (response.errorMessage){
        		$scope.error = response.errorMessage
        	}else{
        		$scope.title = response.title;
        	}
        })
        .error(function(err){
    		$scope.error = "Please check the URL for correctness. URL should include protocol and host."
        });
        }
        
    }])

})();
