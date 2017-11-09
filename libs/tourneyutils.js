'use strict';

class Round {
	constructor(round, matches){
		this._roundNumber = round;
		this._matches = matches;
	}
	
	get roundNumber(){
		return this._roundNumber;
	}
	
	set roundNumber(n){
		if(n){
			this._roundNumber = n;
		}
	}
	
	get matches(){
		return this._matches;
	}
	
	set matches(n){
		if(n){
			this._matches = n;
		}
	}
}

class Result {
	constructor (){
		this._points = 0;
		this._goal = 0;
	}
	
	get points (){
		return this._points;
	}
	set points (p){
		if(p) this._points = p;
	}
	
	get goal (){
		return this._goal;
	}
	set goal (g){
		if(g) this._goal = g;
	}
}

class Match {
	constructor(player1, player2, table, roundNumber){
		this._player1 = player1;
		this._player2 = player2;
		this._table = table;
		
		this._roundNumber = roundNumber;
		
		this._p1Result = new Result();
		this._p2Result = new Result();
	}
	
	get player1(){	return this._player1;}
	set player1(p){	this._player1 = p;}
	
	get player2(){	return this._player2;}
	set player2(p){	this._player2 = p;}
	
	get roundNumber(){return this._roundNumber;}
	set roundNumber(r){	this._roundNumber = r;}

	get table(){return this._table;}
	set table(t){this._table = t;}
	
	get p1Result (){return this._p1Result;}
	set p1Result (result){ this._p1Result = result;}
	
	get p2Result (){return this._p2Result;}
	set p2Result (result){ this._p2Result = result;}
}

class Player  {
	constructor(nickname){
		this._nickname = nickname;
		this._games = [];
	}
	
	get nickname(){	return this._nickname;	}
	set nickname(n){ this._nickname = n; }

	get games(){ return this._games; }
	set games(g){ this._games = g; }
	
	compareTo(p2){
		let comp = 0;
		comp += (this.totalPoints() - p2.totalPoints())*1000;
		if(comp === 0)
		comp += this.totalGoal() - p2.totalGoal();
		console.log('compare result : '+comp);
		return comp;
	}
	
	totalPoints(){
		let total = 0; let this_ = this;
		if(this.games.length > 0){
			this.games.forEach(function(game){
				if(game.player1.nickname === this_._nickname){
					total += game.p1Result.points;
				} else {
					total += game.p2Result.points;
				}
			});
		}
		return total;
	}

	totalGoal(){
		let total = 0; let this_ = this;
		if(this.games.length > 0){
			this.games.forEach(function(game){
				if(game.player1.nickname === this_._nickname){
					total += game.p1Result.goal;
				} else {
					total += game.p2Result.goal;
				}
			});
		}
		return total;
	}	
	
	hasPlayedOnTable(table){
		let hasPlayedOnTable = false;
		if(this._games){
			this._games.forEach(function(game){
				if(game.table === table){
					hasPlayedOnTable = true;
				}
			});
		}
		return hasPlayedOnTable;
	}
	
}

class Tourney {
	constructor(){
		this._players = [];
		this._rounds = [];
	}
	
	get players(){
		return this._players;
	}
	
	set players(p){
		if(p){
			this._players = p;
		}
	}
	
	get rounds(){
		return this._rounds;
	}
	
	set rounds(r){
		if(r){
			this._rounds = r;
		}
	}
	
	createMatches(playersToMatch){
		let this_ = this;
		let roundNumber = this._rounds.length + 1;
		let matchesArray = [], matchedPlayers = [], matchedTables = [];
		
		let maxTable = playersToMatch.length / 2;
		
		this.sortPlayers(playersToMatch);
		
		while(matchesArray.length < maxTable){
			let playerToMatch = playersToMatch.shift();
			console.log('Matching player '+playerToMatch.nickname);
			
			let indexPlayerMatched = -1;
			let match = null;
			playersToMatch.forEach(function(player){
				console.log('Trying to match against player '+player.nickname);
				if(player.games){
					player.games.forEach(function(game){
						if((game.player1.nickname !== playerToMatch.nickname
							&& game.player2.nickname !== playerToMatch.nickname)
							&& indexPlayerMatched === -1){
							indexPlayerMatched = playersToMatch.indexOf(player);
						}
					});
				}
			});
			
			if(indexPlayerMatched === -1){
				indexPlayerMatched = 0;
			}
			match = this_.createMatch(playersToMatch[indexPlayerMatched], playerToMatch, maxTable, matchedTables, roundNumber);
			
			if(match){
				matchesArray.push(match);
			}
			
			playersToMatch.splice(indexPlayerMatched,1);
		}
		
		return matchesArray;
	}
	
	createMatch(player, playerToMatch, maxTable, matchedTables, roundNumber){
		let table = this.findTable(player, playerToMatch, maxTable, matchedTables);
		matchedTables.push(table);
		console.log('Creating new match '+player.nickname+' vs '+ playerToMatch.nickname+' on table '+table);
		return new Match(playerToMatch, player, table, roundNumber);
	}
	
	findTable(player1, player2, maxTable, matchedTables){
		let t = 1;
		let availableTables = [];
		for(t = 1; t <= maxTable; t ++){
			if($.inArray(t, matchedTables) === -1){
				availableTables.push(t);
			}
		}
		
		let firstAvailableTable = availableTables[0];
		availableTables = availableTables.filter(function (t){
			let played = true;
			if(player1.games){
				player1.games.forEach(function(match){
					if(match.table === t){
						console.log(player1.nickname+' already played on table '+t);
						played = false;
					}
				});
			}
			
			if(player2.games){
				player2.games.forEach(function(match){
					if(match.table === t){
						console.log(player2.nickname+' already played on table '+t);
						played = false;
					}
				});
			}
			console.log('filtering table '+t+' : '+!played);
			return played;
		});

		console.log(availableTables.length+' tables available '+availableTables+'. Match set on table '+(availableTables.length > 0 ? availableTables[0] : firstAvailableTable));
		return availableTables.length > 0 ? availableTables[0] : firstAvailableTable;
	}
	
	
	
	sortPlayers(players){
		players.sort(function(player1, player2){
			return player1.compareTo(player2);
		});
	}
}

