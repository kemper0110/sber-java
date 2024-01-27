package org.danil;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final var scanner = new Scanner(System.in);

        System.out.print("Root directory(default=my): ");

        final var defaultDirectory = "file:///C:/Users/Danil/IdeaProjects/sber-java/lab7/plugins/target/classes";
        final var inputDirectory = scanner.nextLine();
        final var rootDirectory = inputDirectory == "" ? defaultDirectory : inputDirectory;

        System.out.print("Iterations number(default=1): ");

        final var defaultIterations = 1;
        final var inputIterations = scanner.nextLine();
        final var iterationsNumber = inputIterations == "" ? defaultIterations : Integer.parseInt(inputIterations);



        final var playerManager = new PlayerManager(rootDirectory);

        final var plugins = new String[]{
                "org.danil.PaperPlayer",
                "org.danil.RandomPlayer",
                "org.danil.RockHaterPlayer"
        };


        for(int i = 0; i < iterationsNumber; ++i) {
            System.out.println("\n\n\t\t\tITERATION " + i);

            var bestPlayer = playerManager.load(plugins[0]);

            for (int j = 1; j < plugins.length; ++j) {
                var oppositePlayer = playerManager.load(plugins[j]);

                System.out.println("Playing " + bestPlayer.getClass().getName() + " vs " + oppositePlayer.getClass().getName());

                while(true) {
                    final var play1 = bestPlayer.play();
                    final var play2 = oppositePlayer.play();
                    System.out.println("Played " + play1 + " and " + play2);

                    if(play1 != play2) {
                        bestPlayer = play1.beats(play2) ? bestPlayer : oppositePlayer;
                        break;
                    }
                    System.out.println("Playing again");
                }

                System.out.println("Winner is " + bestPlayer.getClass().getName() + "\n");
            }
        }
    }
}