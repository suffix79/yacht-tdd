package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class YachtController {

  private final Game game;

  @Autowired
  public YachtController(Game game) {
    this.game = game;
  }

  @PostMapping("/rolldice")
  public String rollDice() {
    game.rollDice();
    return "redirect:/rollresult";
  }

  @GetMapping("/rollresult")
  public String rollResult(Model model) {
    model.addAttribute("score", String.valueOf(game.score()));
    model.addAttribute("roll", RollView.from(game.lastRoll()));
    model.addAttribute("categories", viewOf(game.scoredCategories()));
    return "roll-result";
  }

  private List<ScoredCategoryView> viewOf(List<ScoredCategory> scoredCategories) {
    return scoredCategories.stream()
                           .map(ScoredCategoryView::from)
                           .collect(Collectors.toList());
  }

  @PostMapping("/select-category")
  public String assignRollToCategory(@RequestParam("category") String category) {
    ScoreCategory scoreCategory = ScoreCategory.valueOf(category.toUpperCase());
    game.assignRollTo(scoreCategory);

    return "redirect:/rollresult";
  }
}
