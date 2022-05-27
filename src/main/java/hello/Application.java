package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {

  private String[] commands = new String[]{"F", "R", "L", "T"};

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;

    @Override
    public String toString() {
      return "x: " + this.x + ", " +
              "y: " + this.y + ", " +
              "direction: " + this.direction + ", " +
              "wasHit: " + this.wasHit + ", " +
              "score: " + this.score;
    }
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!";
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {

    Links links = arenaUpdate._links;
    Arena _arena = arenaUpdate.arena;
    List<Integer> _dims = _arena.dims;

    PlayerState self = _arena.state.get(links.self.href);
    Map<String, PlayerState> enemies = _arena.state;
    enemies.remove(links.self.href);

    System.out.println(self.toString());

    for(int i = 0; i < enemies.size(); i++) {
      PlayerState enemy = enemies.get(i);
      if(self.direction == "N" && (enemy.x == self.x) && (enemy.y < self.y)) {
        System.out.println("Shoot");
        return commands[3];
      } else if(self.direction == "E" && (enemy.y == self.y) && (enemy.x > self.x)) {
        System.out.println("Shoot");
        return commands[3];
      }  else if(self.direction == "S" && (enemy.x == self.x) && (enemy.y > self.y)) {
        System.out.println("Shoot");
        return commands[3];
      }  else if(self.direction == "W" && (enemy.y == self.y) && (enemy.x < self.x)) {
        System.out.println("Shoot");
        return commands[3];
      }
    }

    int random = new Random().nextInt(3);
    return commands[random];
  }
}
//{
//        "_links": {
//          "self": {
//            "href": "https://YOUR_SERVICE_URL"
//          }
//        },
//        "arena": {
//          "dims": [4,3], // width, height
//          "state": {
//            "https://A_PLAYERS_URL": {
//              "x": 0, // zero-based x position, where 0 = left
//              "y": 0, // zero-based y position, where 0 = top
//              "direction": "N", // N = North, W = West, S = South, E = East
//              "wasHit": false,
//              "score": 0
//            }
//            ... // also you and the other players
//         }
//   }
//}

