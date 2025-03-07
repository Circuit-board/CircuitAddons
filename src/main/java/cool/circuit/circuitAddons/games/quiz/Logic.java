package cool.circuit.circuitAddons.games.quiz;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cool.circuit.circuitAddons.CircuitAddons.getMenuUtility;

public class Logic {
    public Level generateLevel(Player player) {
        final int question1 = (int) (Math.random() * 100);
        final int question2 = (int) (Math.random() * 100);
        final int answer = (int) question1 * question2;
        final int[] answers = new int[4];
        for(int i = 0; i < answers.length; i++) {
            answers[i] = (int) (Math.random() * 1000000);
        }
        List<Integer> answerList = Arrays.stream(answers)
                .boxed()
                .collect(Collectors.toList());

        return new Level(getMenuUtility(player), answer, answerList, question1, question2);
    }
}
