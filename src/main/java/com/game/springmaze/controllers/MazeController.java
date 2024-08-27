package com.game.springmaze.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.game.springmaze.service.MazeService;

import org.springframework.ui.Model;

//import java.util.Arrays;

@Controller
public class MazeController {
    @Autowired
    MazeService mazeService;

    //Get route (index page)
    @GetMapping("/")
    public String getMethodName(Model model) {
        Maze maze = new Maze(null, null, null);
        model.addAttribute("maze", maze);

        return "game-home-page";
    }

    //Post route (creates the maze and display it)
    @PostMapping("/")
    public String generateMaze(@ModelAttribute("maze") Maze maze, Model model){
        int[][] grid = new int[maze.height()][maze.width()];
        String[][] borders = mazeService.getBordersGrid(maze.width(), maze.height(), maze.seed());

        model.addAttribute("grid", grid);
        model.addAttribute("borders", borders);
        return "game-maze-page";
    }
}
