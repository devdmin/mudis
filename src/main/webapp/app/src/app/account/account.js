angular.module('ngBoilerplate.account', ['ui.router', 'ngResource', 'base64'])
.config(function($stateProvider) {
    $stateProvider.state('register', {
            url:'/register',
            views: {
                'main': {
                    templateUrl:'account/register.tpl.html',
                    controller: 'RegisterCtrl'
                }
            },
            data : { pageTitle : "Registration" }
            }
    );
})
.factory("sessionService", function($state, $http, $base64){
  var session = {};
   session.login = function(data){
     return $http.post("/mudis/login", "username=" + data.name +
           "&password=" + data.password, {
               headers: {'Content-Type': 'application/x-www-form-urlencoded'}
           } ).then(function(data) {
               $state.go("music");
               localStorage.setItem("session", {});
           }, function(data) {
               alert("error logging in");
           });
   };
   session.logout = function(data){
     localStorage.removeItem("session");
     $state.go("home");
   };
  session.isLoggedIn = function(){
     return localStorage.getItem("session") !== null;
   };

  return session;
})
.factory("accountService", function($resource){
        var service = {};
        service.register = function(account, success, failure){
            var Account = $resource("/mudis/rest/accounts");
            Account.save({}, account, success, failure);
        };
        service.getAccountByName = function(accountName) {
        var Account = $resource("/mudis/rest/accounts/:paramAccountName");
        return Account.get({paramAccountName:accountName}).$promise;
        };
        service.userExists = function(account, success, failure) {
        var Account = $resource("/mudis/rest/accounts");
        var data = Account.get({name:account.name, password:account.password}, function() {
            var accounts = data.accounts;
            if(accounts.length !== 0) {
                success(account);
            } else {
                failure();
            }
        },
        failure);
        };
        service.getAllAccounts = function() {
          var Account = $resource("/mudis/rest/accounts");
          return Account.get().$promise.then(function(data) {
            return data.accounts;
          });
        };

        return service;
})
.controller("RegisterCtrl", function($scope, $state, accountService, sessionService) {
  $scope.register = function() {
    accountService.register($scope.account,
           function(returnedData) {
             sessionService.login($scope.account).then(function() {
                       $state.go("music");
                   });
           },
           function() {
               alert("Error registering user");
           });
     };
});
