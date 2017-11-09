let win = require('electron').remote.getCurrentWindow();

$(function(){
    feather.replace();
	var tournoi = new Tourney();

	$('#button-add-round').on('click', addRound);
	
	$('#modal-add-player').on('shown.bs.modal', function () {
		$('#error-nickname').hide();
		$('#player-nickname').trigger('focus');
	});
	
	$('#modal-submit-player').on('click', addPlayer);
	$('#form-player').on('submit', addPlayer);
	$('#nav-tabs-tourney').on('click', '.remove-round', removeRound);
	
	$('#close').on('click', () => {
		win.close();
	});
	
	$('#minimize').on('click', () => {
		win.minimize();
	});
	
	$('#maximize').on('click', () => {
		if(win.isMaximized()){
			win.unmaximize();
		} else {
			win.maximize();
		}
	});
	
	$('#button-import').on('click', ()=>{
		$('#import-file').trigger('click');
	});

	$('#import-file').on('change', importPlayers);

	function addPlayer(e){
		e.stopPropagation; 
		e.preventDefault();
		
		if(tournoi.players){
			let nickExists = false;
			tournoi.players.forEach(function(player){
				if(player.nickname === $('#player-nickname').val()){
					
					nickExists = true;
				}
			});
			
			if(nickExists){
				$('#error-nickname').show();
				return false;
			}
		}
		
		console.log('new player : '+$('#player-nickname').val())
		var player = new Player($('#player-nickname').val());
		tournoi.players.push(player);
		reloadPlayersTable();
		
		$('#player-nickname').val('');
		$('#modal-add-player').modal('hide');
		return true;
		
	}
	
	function reloadPlayersTable(){
		$('#table-rankings').find('tbody').remove();
		
		if(tournoi.players && tournoi.players.length > 0){
			let content = '<tbody>';
			let rank = 1;
			tournoi.sortPlayers(tournoi.players);
			tournoi.players.forEach(function(player){
				content += '<tr>';
				
				content += '<td>' + rank + '</td>';
				content += '<td>' + player.nickname + '</td>';
				content += '<td>' + player.totalPoints() + '</td>';
				content += '<td>' + player.totalGoal() + '</td>';
				if(tournoi.rounds.length > 0){
					content += '<td></td>';
				} else {
					content += '<td><span class="text-danger"><button data-nickname="'+player.nickname+'" class="btn-delete-user"><i data-feather="user-x"></i></button></span></td>';
				}
				content += '</tr>';
				rank++;
			});
			
			content += '</tbody>';
			$('#table-rankings').append(content);
			$('#table-rankings').on('click', '.btn-delete-user' ,(event)=>{
				let nickname = $(event.currentTarget).data('nickname');
				if(nickname){
					let playerToRemoveIndex = -1;
					tournoi.players.forEach(function(player){
						if(player.nickname === nickname){
							playerToRemoveIndex = tournoi.players.indexOf(player);
						}
					});
					
					if(playerToRemoveIndex >= 0){
						tournoi.players.splice(playerToRemoveIndex,1);
						reloadPlayersTable();
					}
				}
			});
		}
		feather.replace();
	}
	
	function savePlayerGames(player){
		player.games = [];
		tournoi.rounds.forEach(function(round){
			round.matches.forEach(function(match){
				if(match.player1.nickname === player.nickname
					|| match.player2.nickname === player.nickname){
					console.log('Pushing game for player '+player.nickname)
					player.games.push(match);
				}
			});
		});
	}
	
	function addRound(){
		if(tournoi.rounds.length > 0){
			tournoi.players.forEach(savePlayerGames);
		}
		
		let matches = tournoi.createMatches(tournoi.players.slice(0));
		let roundNumber = tournoi.rounds.length + 1;
		let round = new Round(roundNumber, matches);
		tournoi.rounds.push(round);
		
		reloadRounds();
	}
	
	function removeRound(e){
		let roundNumber = $(e.target).data('round');
		let indexRound = -1;
		tournoi.rounds.forEach(function(round){
			if(round.roundNumber === roundNumber){
				indexRound = tournoi.rounds.indexOf(round);
			}
		});
		
		if(indexRound !== -1) tournoi.rounds.splice(indexRound,1);
		
		tournoi.players.forEach(function(player){
			indexRound = -1;
			player.games.forEach(function(game){
				if(game.roundNumber === roundNumber){
					indexRound = player.games.indexOf(game);
				}
			});
			if(indexRound !== -1) player.games.splice(indexRound,1)
		});
		reloadRounds();
		$('#nav-tabs-tourney a').last().tab('show');
	}
	
	function reloadRounds(){
		$('.nav-round, .tab-round').remove();

		tournoi.rounds.forEach(function(round){
			if(!$('#tab-pane-round-'+round.roundNumber).length){
				$('#nav-tabs-tourney').append(`	<li class="nav-item nav-round">
													<a class="nav-link" href="#tab-pane-round${round.roundNumber}" data-toggle="tab" role="tab" aria-controls="tab-pane-round${round.roundNumber}" aria-selected="true">
														Round ${round.roundNumber}
														<div class="remove-round pull-right"><i data-round="${round.roundNumber}" class="fa fa-remove"></i></div>
													</a>
												</li>`);
				
				$('.tab-pane .show .active').removeClass('active show');
				let tabPaneRound = `<div class="tab-pane tab-round fade" id="tab-pane-round${round.roundNumber}" role="tabpanel" aria-labelledby="tab-pane-round${round.roundNumber}">
										<table id="table-round${round.roundNumber}" class="table table-striped table-bordered table-round">
								  			<thead>
								  				<tr>
								  					<th>Table</th>
								  					<th>Player 1</th>
								  					<th>Pts player 1</th>
								  					<th>G.A. player 1</th>
								  					<th>Player 2</th>
								  					<th>Pts player 2</th>
								  					<th>G.A. player 2</th>
								  				</tr>
								  			</thead>
								  		</table>
								  	</div>`;
				$('#tabcontent-tourney').append(tabPaneRound);
				
				$('#nav-tabs-tourney a[href="#tab-pane-round'+round.roundNumber+'"]').tab('show');
			}
			
			let content = '<tbody>';
			round.matches.forEach(function(match){
				if(tournoi.rounds.indexOf(round) === tournoi.rounds.length-1){
					content += '<tr>';
					
					content += '<td>' + match.table + '</td>';
					content += '<td>' + match.player1.nickname + '</td>';
					content += '<td><input type="text" class="form-control form-result-points" data-table="'+match.table+'" data-player="'+match.player1.nickname+'" value="'+match.p1Result.points+'"></td>';
					content += '<td><input type="text" class="form-control form-result-goal" data-table="'+match.table+'" data-player="'+match.player1.nickname+'" value="'+match.p1Result.goal+'"></td>';
					content += '<td>' + match.player2.nickname + '</td>';
					content += '<td><input type="text" class="form-control form-result-points" data-table="'+match.table+'" data-player="'+match.player2.nickname+'" value="'+match.p2Result.points+'"></td>';
					content += '<td><input type="text" class="form-control form-result-goal" data-table="'+match.table+'" data-player="'+match.player2.nickname+'" value="'+match.p2Result.goal+'"></td>';
					
					content += '</tr>';
				} else {
					content += '<tr>';
					
					content += '<td>' + match.table + '</td>';
					content += '<td>' + match.player1.nickname + '</td>';
					content += '<td>' + match.p1Result.points + '</td>';
					content += '<td>' + match.p1Result.goal + '</td>';
					content += '<td>' + match.player2.nickname + '</td>';
					content += '<td>' + match.p2Result.points + '</td>';
					content += '<td>' + match.p2Result.goal + '</td>';
					
					content += '</tr>';
				}
				
			});
			content += '</tbody>';
			$('#table-round'+round.roundNumber).append(content);
			
			
			$('.table-round').on('keyup', '.form-result-points', addResultPoints);
			$('.table-round').on('keyup', '.form-result-goal', addResultGoal);
			
			function addResultPoints(e){
				let nickname = $(e.target).data('player');
				let table = $(e.target).data('table');
				let round = tournoi.rounds[tournoi.rounds.length-1];
				round.matches.forEach(function(match){
					if(match.table === table){
						if(match.player1.nickname === nickname){
							match.p1Result.points = Number($(e.target).val());
						} else if(match.player2.nickname === nickname){
							match.p2Result.points = Number($(e.target).val());
						}
					}
				})
			}
			
			function addResultGoal(e){
				let nickname = $(e.target).data('player');
				let table = $(e.target).data('table');
				let round = tournoi.rounds[tournoi.rounds.length-1];
				round.matches.forEach(function(match){
					if(match.table === table){
						if(match.player1.nickname === nickname){
							match.p1Result.goal = Number($(e.target).val());
						} else if(match.player2.nickname === nickname){
							match.p2Result.goal = Number($(e.target).val());
						}
					}
				})
			}
		});
		
		reloadPlayersTable();
	}

    function importPlayers(event) {
    	let data = null;
    	let file = event.target.files[0];
    	if(file){
    		let reader = new FileReader();
        	reader.readAsText(file);
        	reader.onload = function(e) {
        		var csvData = e.target.result;
        		data = $.csv.toArrays(csvData);
        		if(data){
        			for(let i = 1; i <= data.length; i++){
        				var arr = data[i];
        				if(arr){
        					var splitted = arr[0].split(';');
        					if(splitted){
        						tournoi.players.push(new Player(splitted[3]));
        					}
        				}
        			}
        			reloadPlayersTable();
        			$('#import-file').val('');
        		}
        	};
    	}
    	
	}
});

