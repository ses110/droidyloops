package com.example.droidyloops.dloops;

/**
 * Created by sid9102 on 3/16/14.
 * A class for handling click highlighting
 */

public class Square
{
    // The topleft coord
    public float x;
    public float y;

    public int row;
    public int col;

    public Square(float x, float y, int row, int col)
    {
        this.x = x;
        this.y = y;
        this.row = row;
        this.col = col;
    }

    public boolean equals(Square other)
    {
        return other.x == this.x && other.y == this.y;
    }
}
