package com.group0536.puzzlemazing.stores.crazymatch;

import android.util.SparseArray;

import com.group0536.puzzlemazing.R;
import com.group0536.puzzlemazing.actions.Action;
import com.group0536.puzzlemazing.actions.crazymatch.CrazyMatchActions;
import com.group0536.puzzlemazing.dispatcher.Dispatcher;
import com.group0536.puzzlemazing.models.Animal;
import com.group0536.puzzlemazing.models.CrazyMatchBoard;
import com.group0536.puzzlemazing.models.User;
import com.group0536.puzzlemazing.stores.Store;
import com.group0536.puzzlemazing.stores.StoreChangeEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CrazyMatchStore extends Store implements CrazyMatchActions {
    private CrazyMatchBoard board;
    private int score;
    private Animal firstFlip;
    private Animal secondFlip;
    private User player;
    private int pairsLeft;
    private int stepsTaken;
    private boolean isGameOver;
    private static List<Integer> allAnimals;
    private SparseArray<List<Integer>> levelToDimension;
    private static CrazyMatchStore instance;

    protected CrazyMatchStore(Dispatcher dispatcher) {
        super(dispatcher);
        populateAllAnimals();
        board = null;
        score = 0;
        firstFlip = null;
        secondFlip = null;
        // TODO: pass in a player
//        player = player;
        pairsLeft = 0;
        stepsTaken = 0;
        isGameOver = false;
        setLevelToDimension();


    }

    private void setLevelToDimension() {
        List<Integer> dim1 = new ArrayList<>();
        dim1.add(2);
        dim1.add(3);
        levelToDimension = new SparseArray<>();
        levelToDimension.put(1, dim1);

        List<Integer> dim2 = new ArrayList<>();
        dim2.add(2);
        dim2.add(4);
        levelToDimension.put(2, dim2);
    }

    private static void populateAllAnimals() {
        allAnimals = new ArrayList<>(Arrays.asList(R.drawable.butterfly,
                R.drawable.chicken,
                R.drawable.cow,
                R.drawable.dog,
                R.drawable.lizard,
                R.drawable.fish,
                R.drawable.fox,
                R.drawable.frog,
                R.drawable.ladybug,
                R.drawable.lion,
                R.drawable.octopus,
                R.drawable.owl,
                R.drawable.reindeer,
                R.drawable.robin,
                R.drawable.snail,
                R.drawable.turkey));
    }


    public CrazyMatchBoard getBoard() {
        return board;
    }

    public static CrazyMatchStore getInstance(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new CrazyMatchStore(dispatcher);
        }
        return instance;
    }

    @Override
    protected StoreChangeEvent getChangeEvent() {
        return new CrazyMatchChangeEvent();
    }

    @Override
    @Subscribe
    public void onAction(Action action) {
        switch (action.getType()) {
            case FLIP:
                int row = (int) action.getPayloadEntry("row");
                int col = (int) action.getPayloadEntry("col");
                flipAnimal(row, col);
                postChange();
                if ((firstFlip != null) && (secondFlip != null)) {
                    Timer timer_CheckPairs = new Timer();
                    timer_CheckPairs.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (isTheSame(firstFlip, secondFlip)) {
                                updateScore();
                                cancelAnimal(firstFlip);
                                cancelAnimal(secondFlip);
                                clearFlipPair();
                                pairsLeft--;
                            } else {
                                // two flips are not the same
                                clearFlipPair();
                            }
                        }
                    }, 1000);
                }
                // do nothing since the user just flip one animal in the board
                postChange();
                break;
            case SET_BOARD:
                // set board
                setBoard((int) action.getPayloadEntry("level"));
                //postChange();
                break;
        }
    }

    private void setBoard(int level) {
        List<Integer> dimension = levelToDimension.get(level);
        int row = dimension.get(0);
        int col = dimension.get(1);
        int numPair = row * col / 2;
        List<Integer> randomAnimals = randomAnimalsGenerator(numPair);
        Animal[][] animalsInMatchBoard = new Animal[row][col];
        for (int i = 0; i < animalsInMatchBoard.length; i++) {
            Animal[] currentRowOfAnimals = new Animal[numPair];
            Collections.shuffle(randomAnimals);
            for (int j = 0; j < currentRowOfAnimals.length; j++) {
                animalsInMatchBoard[i][j] = new Animal(randomAnimals.get(j), i, j);
            }
            animalsInMatchBoard[i] = currentRowOfAnimals;
        }
        board = new CrazyMatchBoard(animalsInMatchBoard);
    }

    private List<Integer> randomAnimalsGenerator(int num) {
        Random rand = new Random();
        List<Integer> randomAnimals = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int randomIndex = rand.nextInt(allAnimals.size());
            randomAnimals.add(allAnimals.get(randomIndex));
        }
        return randomAnimals;
    }

    private void clearFlipPair() {
        firstFlip = null;
        secondFlip = null;
    }

    private void cancelAnimal(Animal crossedOutAnimal) {
        board.crossOutAnimal(crossedOutAnimal);
    }

    private void updateScore() {
        // update score
    }

    private boolean isTheSame(Animal firstFlip, Animal secondFlip) {
        return firstFlip.equals(secondFlip);
    }

    private void flipAnimal(int row, int col) {
        board.getAnimal(row, col).flipOver();
        stepsTaken += 1;
    }

}
