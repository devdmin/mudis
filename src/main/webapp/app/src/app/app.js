angular.module( 'ngBoilerplate', [
  'templates-app',
  'templates-common',
  'ngBoilerplate.home',
  'ngBoilerplate.account',
  'ngBoilerplate.music',
  'ngBoilerplate.about',
  'ui.router'
])

.config( function myAppConfig ( $stateProvider, $urlRouterProvider ) {
  $urlRouterProvider.otherwise( '/home' );
})

.run( function run () {
})

.controller( 'AppCtrl', function AppCtrl ( $scope, $location, sessionService ) {
  $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
    if ( angular.isDefined( toState.data.pageTitle ) ) {
      $scope.pageTitle = toState.data.pageTitle + ' | ngBoilerplate' ;
    }
  });
  $scope.login = function(){
    sessionService.login($scope.accountToLogin);
  };
  $scope.isLoggedIn = sessionService.isLoggedIn;
  $scope.logout = function(){

    sessionService.logout();
    accountToLogin.name = "";
    accountToLogin.password = "";
  };
})

;
