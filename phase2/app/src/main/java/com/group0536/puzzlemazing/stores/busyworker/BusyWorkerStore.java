package com.group0536.puzzlemazing.stores.busyworker;

import android.graphics.Point;

import com.group0536.puzzlemazing.actions.Action;
import com.group0536.puzzlemazing.dispatcher.Dispatcher;
import com.group0536.puzzlemazing.models.BusyWorkerBitMap;
import com.group0536.puzzlemazing.models.BusyWorkerMap;
import com.group0536.puzzlemazing.stores.Store;
import com.group0536.puzzlemazing.stores.StoreChangeEvent;
import com.group0536.puzzlemazing.stores.crazymatch.CrazyMatchStore;

public class BusyWorkerStore extends Store {

    private BusyWorkerMap map;
    private BusyWorkerBitMap bitmap;
    private static BusyWorkerStore instance;
    private int score;
    private Point currentWorkerPosition;
    private Point currentBoxPosition;

    protected BusyWorkerStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected StoreChangeEvent getChangeEvent() {
        return new BusyWorkerChangeEvent();
    }

    @Override
    public void onAction(Action action) {


    }

    private void initMap(int level) {

    }

    private boolean checkWin() {
        return currentBoxPosition.equals(map.getFlagPosition());
    }

    private boolean checkLose() {
        for (Point deadPosition : map.getDeadPositions()) {
            if (deadPosition.equals(currentBoxPosition)) return true;
        }
        return false;
    }

    private void movement(Point touchPosition) {
        String HorizontalDirection = checkHorizontalPosition(touchPosition);
        String VerticalDirection = checkVerticalPosition(touchPosition);
        switch (HorizontalDirection) {
            case "left":
                moveLeft();
            case "right":
                moveRight();
                break;
        }
        switch (VerticalDirection) {
            case "above":
                moveAbove();
            case "below":
                moveBelow();
                break;
        }
    }

    private String checkVerticalPosition(Point touchPosition) {
        if (touchPosition.y > currentWorkerPosition.y) return "above";
        else if (touchPosition.y == currentWorkerPosition.y) return "below";
        else return "noVerticalMovement";
    }

    private String checkHorizontalPosition(Point touchPosition) {
        if (touchPosition.x > currentWorkerPosition.x) return "right";
        else if (touchPosition.x == currentWorkerPosition.x) return "left";
        else return "noHorizontalMovement";
    }

    private void moveAbove() {
        if (BoxAboveWorker() && !(WallAboveWorker())) {
            currentBoxPosition.y = currentBoxPosition.y + 1;
            currentWorkerPosition.y = currentWorkerPosition.y + 1;
        } else currentWorkerPosition.y = currentWorkerPosition.y + 1;
    }

    private void moveBelow() {
        if (BoxBelowWorker() && !(WallBelowWorker())) {
            currentBoxPosition.y = currentBoxPosition.y - 1;
            currentWorkerPosition.y = currentWorkerPosition.y - 1;
        } else currentWorkerPosition.y = currentWorkerPosition.y - 1;
    }

    private void moveLeft() {
        if (BoxLeftToWorker() && !(WallLeftToWorker())) {
            currentBoxPosition.x = currentBoxPosition.x - 1;
            currentWorkerPosition.x = currentWorkerPosition.x - 1;
        } else currentWorkerPosition.x = currentWorkerPosition.x - 1;
    }

    private void moveRight() {
        if (BoxRightToWorker() && !(WallRightToWorker())) {
            currentBoxPosition.x = currentBoxPosition.x + 1;
            currentWorkerPosition.x = currentWorkerPosition.x + 1;
        } else currentWorkerPosition.x = currentWorkerPosition.x + 1;
    }

    private boolean WallAboveWorker(){
        for (Point wallPosition : map.getWallPositions()) {
            if (wallPosition.y == currentBoxPosition.y + 1){
                return true;
            }
        }
        return false;
    }

    private boolean WallLeftToWorker(){
        for (Point wallPosition : map.getWallPositions()) {
            if (wallPosition.x == currentBoxPosition.x - 1){
                return true;
            }
        }
        return false;
    }

    private boolean WallRightToWorker(){
        for (Point wallPosition : map.getWallPositions()) {
            if (wallPosition.x == currentBoxPosition.x + 1){
                return true;
            }
        }
        return false;
    }

    private boolean WallBelowWorker(){
        for (Point wallPosition : map.getWallPositions()) {
            if (wallPosition.y == currentBoxPosition.y - 1){
                return true;
            }
        }
        return false;
    }

    private boolean BoxAboveWorker() {
        return currentBoxPosition.x == currentWorkerPosition.x &&
                currentBoxPosition.y == currentWorkerPosition.y + 1;
    }

    private boolean BoxBelowWorker() {
        return currentBoxPosition.x == currentWorkerPosition.x &&
                currentBoxPosition.y == currentWorkerPosition.y - 1;
    }

    private boolean BoxRightToWorker() {
        return currentBoxPosition.x == currentWorkerPosition.x + 1 &&
                currentBoxPosition.y == currentWorkerPosition.y;
    }

    private boolean BoxLeftToWorker() {
        return currentBoxPosition.x == currentWorkerPosition.x - 1 &&
                currentBoxPosition.y == currentWorkerPosition.y;
    }

    public static BusyWorkerStore getInstance(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new BusyWorkerStore(dispatcher);
        }
        return instance;
    }
}