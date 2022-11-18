package com.example.alpharelease.Shogi;

import com.example.alpharelease.GameFramework.GameMainActivity;
import com.example.alpharelease.GameFramework.LocalGame;

import com.example.alpharelease.GameFramework.gameConfiguration.GameConfig;
import com.example.alpharelease.GameFramework.gameConfiguration.GamePlayerType;
import com.example.alpharelease.GameFramework.infoMessage.GameState;
import com.example.alpharelease.GameFramework.players.GamePlayer;
import com.example.alpharelease.GameFramework.utilities.Logger;
import com.example.alpharelease.GameFramework.utilities.Saving;
import com.example.alpharelease.R;

import java.util.ArrayList;

/**
 *
 * @author Kathryn Weidman
 * @author Emma Kelly
 * @author Brent Torres
 * @author Matthew Tran
 *
 * @version 10/14/2022
 *
 * */

public class MainActivity extends GameMainActivity {

    private static final String TAG = "MainActivity";

    @Override
    public GameConfig createDefaultConfig() {
        //ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new ShogiHumanPlayer(name, R.layout.activity_main);
            }
        });

        playerTypes.add(new GamePlayerType("Computer Player (dumb)") {
            public GamePlayer createPlayer(String name) {
                return new ShogiComputerPlayer(name);
            }
        });


        GameConfig defaultConfig = new GameConfig(playerTypes, 2,2, "Shogi", 1285);

        defaultConfig.addPlayer("Human", 0); // first shogi human player
        defaultConfig.addPlayer("Computer", 1); // first computer (dumb) player

        // Initial info set
        defaultConfig.setRemoteData("Remote Player", "", 1); // red-on-yellow GUI

        return defaultConfig;
    }

    /**
     * createLocalGame
     *
     * Creates a new game that runs on the server tablet,
     * @param gameState
     * 				the gameState for this game or null for a new game
     *
     * @return a new, game-specific instance of a sub-class of the LocalGame
     *         class.
     */
    @Override
    public LocalGame createLocalGame(GameState gameState){
        if(gameState == null)
            return new ShogiLocalGame();
        return new ShogiLocalGame((ShogiGameState) gameState);
    }

    /**
     * saveGame, adds this games prepend to the filename
     *
     * @param gameName
     * 				Desired save name
     * @return String representation of the save
     */
    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
    }

    /**
     * loadGame, adds this games prepend to the desire file to open and creates the game specific state
     * @param gameName
     * 				The file to open
     * @return The loaded GameState
     */
    @Override
    public GameState loadGame(String gameName){
        String appName = getGameString(gameName);
        super.loadGame(appName);
        Logger.log(TAG, "Loading: " + gameName);
        return (GameState) new ShogiGameState((ShogiGameState) Saving.readFromFile(appName, this.getApplicationContext()));
    }


}