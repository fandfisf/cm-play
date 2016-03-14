'use strict';
(function(){
    angular.module('scraperApp',[])
    
    // define controller and inject $http service as dependency.
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
        		$scope.title = response.title; // Assign data received to $scope.data
        	}
        })
        .error(function(err){
    		$scope.error = "Please check the URL for correctness. URL should include protocol and host."
        });
        }
        
    }])

})();
