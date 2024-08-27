package com.game.springmaze.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Arrays;

import org.springframework.stereotype.Service;

@Service
public class MazeService {
    //Recursive Backtracking Algorithm
    private static final int N = 1; //North
    private static final int S = 2; //South
    private static final int E = 4; //East
    private static final int W = 8; //West

    //Directions in X
    private static final Map<Integer, Integer> DX = 
        new HashMap<Integer, Integer>(){{
            put(E, 1); put(W, -1); put(N, 0); put(S, 0);
        }};
    
    //Directions in Y
    private static final Map<Integer, Integer> DY = 
        new HashMap<Integer, Integer>(){{
            put(E, 0); put(W, 0); put(N, -1); put(S, 1);
        }};
    
    //Opposite Directions
    private static final Map<Integer, Integer> OPPOSITE = 
        new HashMap<Integer, Integer>(){{
            put(E, W); put(W, E); put(N, S); put(S, N);
        }};


    //Recursive Backtracking
    private static void generateMaze(int cx, int cy, 
        int width, int height, int [][] grid, int seed){
        List<Integer> directions = Arrays.asList(N, S, E, W);
        if(seed != -1){
            Collections.shuffle(directions, new Random(seed));
            seed *= seed;
        }else
            Collections.shuffle(directions);

        for(int direction : directions){
            int nx = cx + DX.get(direction);
            int ny = cy + DY.get(direction);
            if(ny >= 0 && ny < height && nx >= 0 && nx < width && grid[ny][nx] == 0){
                grid[cy][cx] |= direction;
                grid[ny][nx] |= OPPOSITE.get(direction);
                generateMaze(nx, ny, width, height, grid, seed);
            } 
        }
    }

    //Recives width and height
    //Returns the numeric maze grid
    public int[][] getNumericGrid(int width, int height, int seed){
        int[][] grid = new int [height][width];
        generateMaze(0, 0, width, height, grid, seed);
        return grid;
    }

    //Recives width and height
    //Returns the graphical representation of the maze using ASCII
    public String getGraphicGrid(int width, int height, int seed){
        String graphicGrid = "";
        int[][] grid = new int [height][width];
        generateMaze(0, 0, width, height, grid, seed);

        graphicGrid += " ";
        for (int i = 0; i < width * 2 - 1; i++) {
            graphicGrid += "_";
        }

        //Printing using ASCII, | for walls, - for floors
        graphicGrid += "\n";
        for (int y = 0; y < height; y++) {
            graphicGrid += "|";
            for (int x = 0; x < width; x++) {
                graphicGrid += (grid[y][x] & S) != 0 ? " " : "_";
                if ((grid[y][x] & E) != 0) {
                    graphicGrid += ((grid[y][x] | grid[y][x + 1]) & S) != 0 ? " " : "_";
                }else{
                    graphicGrid += "|";
                }
            }
            graphicGrid += "\n";
        }
        return graphicGrid;
     }

    //Recives width and height
    //Returns the maze grid borders in String format: N, S, E, W
    public String[][] getBordersGrid(int width, int height, int seed){
        String[][] borders = new String[height][width];

        int[][] grid = new int [height][width];
        generateMaze(0, 0, width, height, grid, seed);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //External borders
                if(borders[y][x] == null)
                    borders[y][x] = "";
                if(y == 0) borders[y][x] += "N";
                if(y == (height - 1)) borders[y][x] += "S";
                if(x == 0) borders[y][x] += "W";
                if(x == (width - 1)) borders[y][x] += "E";

                //Internal Borders
                if(!borders[y][x].contains("S")){
                    borders[y][x] += (grid[y][x] & S) != 0 ? "" : "S";
                    borders[y+1][x] = (grid[y][x] & S) != 0 ? "" : "N";
                }
                if ((grid[y][x] & E) != 0) {
                    if(borders[y][x].contains("S")) continue;
                    borders[y][x] += ((grid[y][x] | grid[y][x + 1]) & S) != 0 ? "" : "S";
                    borders[y+1][x] = (grid[y][x] & S) != 0 ? "" : "N";
                }else if(!borders[y][x].contains("E")){
                    borders[y][x] += "E";
                    borders[y][x+1] = "W";
                }
            }
        }

        return borders;
     }
}
