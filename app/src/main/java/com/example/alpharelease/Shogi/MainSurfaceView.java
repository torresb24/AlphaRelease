package com.example.alpharelease.Shogi;

import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.example.alpharelease.GameFramework.LocalGame;
import com.example.alpharelease.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Kathryn Weidman
 * @author Emma Kelly
 * @author Brent Torres
 * @author Matthew Tran
 *
 * @version 11/09/2022
 *
 * */

public class MainSurfaceView extends SurfaceView implements View.OnTouchListener {

    private int toggle;
    private int imagesize;
    private int buffersizeHoriz;
    private int buffersizeVert;
    private int xcord;
    private int ycord;
    private int tileSize;
    LocalGame lg;
    Paint paint;
    private int currID;
    private int lever;
    private int Piecex,Piecey;
    private int noMove;
    private Paint imgPaint;
    private Paint P2paint;
    private ShogiLocalGame game;
    private ShogiGameState state;
    Matrix transform;
    private ArrayList<Integer> holdCords;
    public MainSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs); // Call parent constructor
        // 2 Enable drawing
        setWillNotDraw(false);
        // 3 setup any required member variables
        imgPaint = new Paint();
        imgPaint.setColor(Color.BLACK);
        toggle = 0;
        imagesize = 1050;
        buffersizeHoriz = 563;
        buffersizeVert = 14;
        xcord = -1;
        ycord = -1;
        tileSize = imagesize/9;
        paint = new Paint();
        P2paint = new Paint();
        paint.setARGB(255/2, 255, 145, 164);
        P2paint.setARGB(255/4, 199, 0, 200);
        currID = 0;
        lever = 0;
        Piecex = Piecey = -1;
        noMove =0;
        holdCords = new ArrayList<Integer>();
        state = new ShogiGameState();
        game = new ShogiLocalGame(state);
        transform = new Matrix();
        transform.preRotate(180);
        //spots = new ArrayList<Spot>(); // Optional to repeat or not repeat the type <Spot>
    }

    // 4 tell the view what to draw/how to draw
    protected void onDraw(Canvas canvas) {
        //TODO: Use state to draw the correct things
        // DO NOT, if at all possible, allocate anything in the draw method
        // METHOD (memory use optimization)
        // This method could run 100+ times per second (and potentially crash
        // -- a device if garbage collection is not fast enough

        //TODO: Move these images into the constructor so that we only have to create them once
        //draw the main board
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.full_board);
        //canvas.drawBitmap(image, -1*buffersizeHoriz + tileSize, -1*buffersizeVert, imgPaint);

        //draw the graveyard


        //draw the initial setup for player 1
        for(Piece p: state.pieces1){
            image = BitmapFactory.decodeResource(getResources(), p.pieceType.getID());
            canvas.drawBitmap(image, ((tileSize) * p.getCol()), ((tileSize) * p.getRow()), imgPaint);
        }

        //draw the initial set up for player 2
        for(Piece p: state.pieces2){
            image = BitmapFactory.decodeResource(getResources(), p.pieceType.getID());
            transform.setTranslate(((tileSize) * p.getCol()),((tileSize) * p.getRow()));
            canvas.drawBitmap(image, transform, imgPaint);
            canvas.drawRect(tileSize * p.getCol() + buffersizeHoriz/25, tileSize * p.getRow() , (tileSize * p.getCol()) + tileSize + buffersizeHoriz/25, (tileSize * p.getRow()) + tileSize ,P2paint);
        }

        //draw the possible moves
        for(int i = 0; i < state.cords.size(); i += 2){
            canvas.drawRect(tileSize * state.cords.get(i) + buffersizeHoriz/25, tileSize * state.cords.get(i + 1) , (tileSize * state.cords.get(i)) + tileSize + buffersizeHoriz/25, (tileSize * state.cords.get(i + 1)) + tileSize , paint);
        }
        // Moving the draw down here lets us draw on TOP of the image / circle above
        // For spots, can use for integer based loop or for each

        // button that causes the image or the circle appears
    }

    @Override
    public boolean onTouch(View view, MotionEvent e){
        //TODO: This is where to send moves to the game using game.sendAction(new ShogiAction)
        if (e.getActionMasked() == MotionEvent.ACTION_DOWN){
            float x = e.getX();
            float y = e.getY();

            if(x >= 0 && x <= imagesize && y <= imagesize && y >= 0){
                // X cord

                //TODO: FOR LOOP THIS (OPTIMIZATION)
                if(x < tileSize){xcord = 0;}
                else if(x > tileSize && x < tileSize*2){xcord = 1;}
                else if(x > tileSize*2 && x < tileSize*3){xcord = 2;}
                else if(x > tileSize*3 && x < tileSize*4){xcord = 3;}
                else if(x > tileSize*4 && x < tileSize*5){xcord = 4;}
                else if(x > tileSize*5 && x < tileSize*6){xcord = 5;}
                else if(x > tileSize*6 && x < tileSize*7){xcord = 6;}
                else if(x > tileSize*7 && x < tileSize*8){xcord = 7;}
                else if(x > tileSize*8 && x < tileSize*9){xcord = 8;}
                // Y cord
                if(y < tileSize ){ycord = 0;}
                else if(y > tileSize  && y < tileSize *2){ycord = 1;}
                else if(y > tileSize *2 && y < tileSize *3){ycord = 2;}
                else if(y > tileSize*3 && y < tileSize *4){ycord = 3;}
                else if(y > tileSize *4 && y < tileSize *5){ycord = 4;}
                else if(y > tileSize *5 && y < tileSize *6){ycord = 5;}
                else if(y > tileSize *6 && y < tileSize *7){ycord = 6;}
                else if(y > tileSize *7 && y < tileSize *8){ycord = 7;}
                else if(y > tileSize *8 && y < tileSize *9){ycord = 8;}
                noMove = 2;
                if (lever == 0) { // first click""
                 //   System.out.println(state.getTurn());
                    if(state.getTurn()){
                        for(Piece p : state.pieces1){
                            if(p.getCol() == xcord && p.getRow() == ycord){
                                currID = p.pieceType.getID();
                                Piecex = p.getCol();
                                Piecey = p.getRow();
                                state.cords.clear();
                                state.cords = game.callCorrectMovement(currID,state.getTurn(),Piecex,Piecey);
                                lever = 1;
                                break;
                            } // if piece matches selected cords
                        } // for Piece p
                        // CHECK IF ITS OWN PIECE //
                        /**
                        for(Piece p: state.pieces1){
                            for(int k = 0; k < state.cords.size(); k+=2){
                                if(p.getRow() == state.cords.get(k) && p.getCol() == state.cords.get(k+1)){
                                    state.cords.set(k+1,-10);
                                    state.cords.set(k,-10);
                                }
                            }
                        }
                        for(int k = 0; k < state.cords.size(); k++){
                            if(state.cords.get(k) >= 0 ){
                                holdCords.add(state.cords.get(k));
                            }
                        }
                        state.cords.clear();
                        state.cords = holdCords;*/
                        // CHECK IF ITS OWN PIECE //
                        invalidate();
                    }
                    // Dumb AI Playing
                    else if(!state.getTurn()){
                        int randIndex = -1;
                        Piecex = -1;
                        Piecey = -1;
                        Random rand = new Random();
                        while(state.cords.isEmpty()){
                            randIndex = rand.nextInt(state.pieces2.size());
                            Piecex = state.pieces2.get(randIndex).getCol();
                            Piecey = state.pieces2.get(randIndex).getRow();
                            currID = state.pieces2.get(randIndex).pieceType.getID();
                            state.cords = game.callCorrectMovement(currID,state.getTurn(),Piecex,Piecey);
                            if(currID == R.drawable.promoted_bishop || currID == R.drawable.promoted_knight ||
                                    currID == R.drawable.promoted_lance ||
                                    currID == R.drawable.promoted_pawn ||
                                    currID == R.drawable.promoted_rook ||
                                    currID == R.drawable.promoted_silv_gen
                            ){
                                state.cords.clear();
                            }
                        }
                        randIndex = 1;
                        while(randIndex%2 != 0){
                            randIndex = rand.nextInt(state.cords.size());
                        }
                        for(Piece p: state.pieces2){
                            if(Piecex == p.getCol() && Piecey == p.getRow() && p.pieceType.getID() == currID){
                                p.setCol(state.cords.get(randIndex));
                                p.setRow(state.cords.get(randIndex+1));
                            }
                        }
                        holdCords.clear();
                        for(int j = 0; j < state.pieces1.size()-1; j++){
                            if(state.pieces1.get(j).getCol() == state.cords.get(randIndex) && (state.pieces2.get(j).getRow() == state.cords.get(randIndex+1))){
                                holdCords.add(j);
                            }
                        }
                        Collections.sort(holdCords);
                        Collections.reverse(holdCords);
                        for(Integer l : holdCords){
                            state.pieces1.remove(l);
                        }
                        state.cords.clear();
                        if(game.checkMate(state.getTurn())){
                            /**GAME END*/
                            System.exit(0);
                        }
                        state.setTurn(!state.getTurn());
                        invalidate();
                    }
                } // if lever == 0
                else if(lever == 1){
                    for(int i = 0; i < state.cords.size(); i+= 2){
                        if(state.cords.get(i) == xcord && state.cords.get(i+1) == ycord){
                            noMove = 1;
                            if(state.getTurn()){
                                for(Piece p : state.pieces1){
                                    if(p.pieceType.getID() == currID && p.getCol() == Piecex && p.getRow() == Piecey){
                                        p.setCol(xcord);
                                        p.setRow(ycord);
                                        // flip turn aswell
                                       // state.changeTurn();
                                        holdCords.clear();
                                        for(int j = 0; j < state.pieces2.size(); j++){
                                            if(state.pieces2.get(j).getCol() == xcord && (state.pieces2.get(j).getRow() == ycord)){
                                                state.pieces2.remove(j);
                                                //holdCords.add(j);
                                            }
                                        }
                                       /** Collections.sort(holdCords);
                                        Collections.reverse(holdCords);
                                        for(Integer l : holdCords){
                                            state.pieces2.remove(l);
                                        }*/

                                        lever = 0;
                                        state.cords.clear();
                                        if(game.checkMate(state.getTurn())){
                                            /**GAME END*/
                                            System.exit(0);
                                        }
                                        state.setTurn(!state.getTurn());
                                        invalidate();
                                    }
                                }
                            }
                            break;
                        }
                    } // for cords
                    if(noMove == 2){
                        state.cords.clear();
                        lever = 0;
                        invalidate();
                    }
                } // if lever == 1
                return true;
            }
        }
        invalidate();
        return false;
    }

    public void setLocalGame(LocalGame g) {
        lg = g;
    }

    public void setGameState(ShogiGameState g) {
        state = g;
    }
}