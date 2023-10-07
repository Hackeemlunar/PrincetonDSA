package turing;

public class Tape {
     private Cell currentCell; // cell is not defined in the project as well
    private Cell head;

    // Constructor to create a tape with a single cell initially
    public Tape() {
        currentCell = new Cell(); // create a new cell when the tape is initialized
        currentCell.content = ' ';// set the current cells content to an empty char
        head = currentCell;
    } //????

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
        Cell cell = new Cell();
        Cell oldFirst = currentCell;
        if (currentCell != null) {
            currentCell = currentCell.prev;
            if (currentCell == null) {
                cell.next = oldFirst;
                oldFirst.prev = cell;
                currentCell = cell;
            }
        }
        if (currentCell.prev == null) head = currentCell;

    }

    // Moves the current cell one position to the right along the tape
    public void moveRight() {
        // moving to the right is the same as getting the next cell
        // so we check if the next cell is null, if it is null it means it has no last to the right so we create a
        // a new cell and set the next of the current cell to the new next cell.
        Cell cell = new Cell();
        Cell oldLast = currentCell;
        if (currentCell != null) {
            currentCell = currentCell.next;
            if (currentCell == null) {
                cell.prev = oldLast;
                oldLast.next = cell;
                currentCell = cell;
            }
        }

    }

    // Returns a String consisting of the chars from all the cells on the tape, read from left to right,
    // excluding leading or trailing blank characters
    public String getTapeContents() {
        StringBuilder tapeContents = new StringBuilder();
        Cell leftmostCell = head;
        while (leftmostCell != null) {
            tapeContents.append(Character.toString(leftmostCell.content));
            leftmostCell = leftmostCell.next;
        }

        return tapeContents.toString().trim();

    }
}
