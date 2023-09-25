package turing;
public class Tape {
    private Cell currentCell; // cell is not defined in the project as well

    // Constructor to create a tape with a single cell initially
    public Tape() {
        currentCell = new Cell(); // create a new cell when the tape is initialized

        // since this is the first cell, it next points to null, its not really necessary but for understanding sake
        currentCell.next = null;

        // since this is the first cell, it prev points to null, its not really necessary but for understanding sake
        currentCell.prev = null;

        currentCell.content = ' ';  // set the current cells content to an empty char
    }

    // Returns the pointer that points to the current cell
    public Cell getCurrentCell() {
        return currentCell; // This is okay
    }

    // returns the char from the current cell.
    public char getContent() {
        return currentCell.content;
    }

    // Changes the char in the current cell to the specified value
    public void setContent(char data) {
        currentCell.content = data;
    }

    // Moves the current cell one position to the left along the tape
    public void moveLeft() {
        // moving to the left is the same as getting the previous cell
        // so we check if the previous cell is null, if it is null it means it has no previous so we create a
        // a new cell and set the previous of the current cell to the new previous cell.
        if (currentCell.prev == null) {
            Cell newCell = new Cell();
            currentCell.prev = newCell;
        }
        currentCell = currentCell.prev;
    }

    // Moves the current cell one position to the right along the tape
    public void moveRight() {
        // moving to the right is the same as getting the next cell
        // so we check if the next cell is null, if it is null it means it has no last to the right so we create a
        // a new cell and set the next of the current cell to the new next cell.
        if (currentCell.next == null) {
            Cell newCell = new Cell();
            currentCell.next = newCell;
        }
        currentCell = currentCell.next;
    }

    // Returns a String consisting of the chars from all the cells on the tape, read from left to right,
    // excluding leading or trailing blank characters
    public String getTapeContents() {
        StringBuilder tapeContents = new StringBuilder();
        Cell leftmostCell = getLeftmostCell();
        while (leftmostCell.next != null) {
            tapeContents.append(Character.toString(leftmostCell.content));
            leftmostCell = leftmostCell.next;
        }

        return tapeContents.toString();

    }

    //returns the last cell in the tape
    private Cell getLeftmostCell() {
        Cell lastCell = currentCell;
        while (lastCell != null) {
            lastCell = lastCell.prev;
        }
        return lastCell;
    }
}
// ?
// sure