var app = angular.module("myApp", ['ngMaterial']);

app.controller("FirstCtrl", function($scope, $http, $mdToast, $mdDialog) {
	
	$http.get("/clockhistory").then(successCallback);
	
	function successCallback(response){
	    $scope.clocks = response.data;
	}
	
	var last = {
		      bottom: false,
		      top: true,
		      left: false,
		      right: true
		    };

		  $scope.toastPosition = angular.extend({},last);

		  $scope.getToastPosition = function() {
		    sanitizePosition();

		    return Object.keys($scope.toastPosition)
		      .filter(function(pos) { return $scope.toastPosition[pos]; })
		      .join(' ');
		  };

		  function sanitizePosition() {
		    var current = $scope.toastPosition;

		    if ( current.bottom && last.top ) current.top = false;
		    if ( current.top && last.bottom ) current.bottom = false;
		    if ( current.right && last.left ) current.left = false;
		    if ( current.left && last.right ) current.right = false;

		    last = angular.extend({},current);
		  }
	
	$scope.listClocks = function(){
		$http.get("/clockhistory").then(successCallback, errorCallback);
		
		function successCallback(response){
		    $scope.clocks = response.data;
		}
		function errorCallback(){
		    alert("error");
		}
	};
	
	$scope.postClock = function(time){
		
		var request = '{ "time": "' + time + '" }'
		
		$http.post("/clock", request).then(successCallback, errorCallback);
		
		function successCallback(response){
			
			var pinTo = $scope.getToastPosition();

		    $mdToast.show(
		    	      $mdToast.simple()
		    	        .textContent('Ângulo do clock criado: ' + response.data.angle + 'º')
		    	        .position(pinTo )
		    	        .hideDelay(1500)
		    	    );
		     
		}
		function errorCallback(){
		    alert("Alguma coisa deu errado, veja se a hora está no formato 'HH:mm', ou se é uma hora possível");
		}
	};
	
	$scope.getClock = function(id){
		$http.get("/clockhistory/" + id).then(successCallback, errorCallback);
		
		
		function successCallback(response){
			$scope.clok = response.data;
			var clk = response.data;
			$mdDialog.show(
				      $mdDialog.alert()
				        .parent(angular.element(document.querySelector('#popupContainer')))
				        .clickOutsideToClose(true)
				        .title('Detalhes de clock ' + id)
				        .textContent("id: " + clk.id
				        			 + ", time: " + clk.time
				        			 + ", createdAt: " + clk.createdAt
				        			 + ", angle: " + clk.angle)
				        .ok('ok')
				    );
		}
		function errorCallback(){
			alert("error");
		}
	};
});