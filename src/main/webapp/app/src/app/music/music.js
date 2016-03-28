angular.module("ngBoilerplate.music", ['ui.router', 'ngResource', 'ngBoilerplate.account', 'hateoas', 'base64'])
.config(function($stateProvider){
  $stateProvider.state('music', {
    url:'/music',
    views: {
      'main': {
        templateUrl:'music/music.tpl.html',
        controller: 'MusicCtrl'
      }
    },
    data: { pageTitle: "Music" }
    }
  );
})
.factory('musicService', function($resource, $q,$http, $base64){
  var service = {};

  service.getLatestMusic = function(){
    var Musics = $resource("/mudis/rest/music/latest/10");
    return Musics.get();
  };

  service.getTopMusic = function(){
    var Musics = $resource("/mudis/rest/music/top/10");
    return Musics.get();
  };

  service.addMusic = function(accountName, musicData, $state){
    var Music = $resource("/mudis/rest/accounts/:paramAccountName/music");
    return Music.save({paramAccountName: accountName}, musicData);
  };

  service.rateMusic = function($state, musicId, usersRating){
    console.log(usersRating);
    return $http.post("/mudis/rest/music/"+musicId+"/rate", "thumbUp=" + usersRating, {
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
          } ).then(function(data) {
            console.log("sent" + data);
          }, function(data) {
              console.log("error " + data);
          });
  };

  service.isRated = function(musicId){
    var Music = $resource("/mudis/rest/music/:paramMusicId/isRated");
    return Music.get({paramMusicId: musicId});
  };

  return service;
})
.factory('authService', function($resource, $q){
  var service = {};
  service.getLoggedUser = function(){
    var Account = $resource("/mudis/rest/accounts/loggedUser");
    return Account.get();
  };
  return service;
})
.controller("MusicCtrl", function($scope, musicService, authService, $sce, $window){
  var accountName;
  var value = 0;
  $scope.info = "Highest-rated";
   musicService.getLatestMusic().$promise.then(function(data){
     $scope.musics = data.music;

     angular.forEach($scope.musics,function(value,index){
       musicService.isRated(value.rid).$promise.then(function(data){
          value.isRated = data.isRated;
       });
     }
    );
   });

  $scope.splitLink = function(text){
    return $sce.trustAsResourceUrl("https://www.youtube.com/embed/" + text);
  };

  authService.getLoggedUser().$promise.then(function(data){

    accountName = data.name;
    $scope.name = data.name;

  });

  $scope.addLink = function(link){
    musicService.addMusic(accountName, {link: link});
      $window.location.reload();
  };

  $scope.thumbUp = function(musicId){
    musicService.rateMusic(musicId, true);
  };
  $scope.thumbDown = function(musicId){
    musicService.rateMusic(musicId, false);
  };
  $scope.changeSource = function(){
    value ^= true;
    if(value === 0){
      $scope.info = "Highest-rated";

      musicService.getLatestMusic().$promise.then(function(data){
        $scope.musics = data.music;

        angular.forEach($scope.musics,function(value,index){
          musicService.isRated(value.rid).$promise.then(function(data){
             value.isRated = data.isRated;
          });
        }
       );
      });

    }
    if(value === 1){

      $scope.info = "Newest music";
      musicService.getTopMusic().$promise.then(function(data){
        $scope.musics = data.music;

        angular.forEach($scope.musics,function(value,index){
          musicService.isRated(value.rid).$promise.then(function(data){
             value.isRated = data.isRated;
          });
        }
       );
      });


    }
  };
})

;
