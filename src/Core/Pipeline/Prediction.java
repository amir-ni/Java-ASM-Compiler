package Core.Pipeline;

import Core.Instructions.beq;

/*
-fully associative

instruction beq $8,$9,-3 at address 0x400014, maps to index 5
branches to address 0x4194316
prediction is: do not take...
branch not taken, prediction was correct
 */
public class Prediction { // 2-bit prediction
    private beq instruction;
    private int correctPredictions = 0;
    private int wrongPredictions = 0;
    private States state;

    public Prediction(beq instruction) {
        this.instruction = instruction;
        state = States.WeaklyNotTaken;
    }

    public enum States {
        StronglyNotTaken, WeaklyNotTaken, WeaklyTaken, StronglyTaken
    }

    public beq getInstruction() {
        return instruction;
    }

    public boolean isTaken(){
        if (state == States.StronglyNotTaken || state == States.WeaklyNotTaken)
            return false;
        return true;
    }

    public void setTaken(boolean taken){
        if (state == States.StronglyNotTaken){
            if (taken)
                state = States.WeaklyNotTaken;
        }else if (state == States.WeaklyNotTaken){
            if (taken)
                state = States.WeaklyTaken;
            else
                state = States.StronglyNotTaken;
        }else if (state == States.WeaklyTaken){
            if (taken)
                state = States.StronglyTaken;
            else
                state =States.WeaklyNotTaken;
        }else{
            if (!taken)
                state = States.WeaklyTaken;
        }
    }

    public double getSuccessPercentage(){
        double p =  correctPredictions *1.0 / (correctPredictions *1.0 + wrongPredictions);
        return p;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public int getCorrectPredictions() {
        return correctPredictions;
    }

    public int getWrongPredictions() {
        return wrongPredictions;
    }

    public void setCorrectPredictions() {
        this.correctPredictions++;
    }

    public void setWrongPredictions() {
        this.wrongPredictions++;
    }
}
