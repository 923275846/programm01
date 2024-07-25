package Programm1.src;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WalkSAT {
    private List<Clauses> clauses;
    private List<Variables> variables;
    //private double p=1; // 反转的概率Probability of randomly flipping a variable 
    private int maxFlips; // 最大反转数量，设置为n的平方；Maximum number of flips allowed

    //构造方法
    public WalkSAT(List<Clauses> clauses, List<Variables> variables) {
        this.clauses = clauses;
        this.variables = variables;
        
        this.maxFlips = variables.size()*variables.size();
    }

   //是否满足
    public boolean isSatisfied() {
        for (Clauses clause : clauses) {
            boolean aValue = clause.getVar1().getValue() == clause.getB1();
            boolean bValue = clause.getVar2().getValue() == clause.getB2();
            if (!aValue && !bValue) {
                return false;
            }
        }
        return true;
    }

    //解可满足性
    public boolean solve() {
        Random random = new Random();

        // 用random的方式初始赋值Initialize variables with random values
        for (Variables var : variables) {
            var.setValue(random.nextBoolean());
        }

        for (int i = 0; i < maxFlips; i++) {
            if (isSatisfied()) {
                return true;
            }

            // 随意选择一个不满足的子句Pick a random unsatisfied clause
            Clauses unsatisfiedClause = null;
            while (unsatisfiedClause == null) {
                Clauses clause = clauses.get(random.nextInt(clauses.size()));
                boolean aValue = clause.getVar1().getValue() == clause.getB1();
                boolean bValue = clause.getVar2().getValue() == clause.getB2();
                if (!aValue && !bValue) {
                    unsatisfiedClause = clause;
                }
            }

            // 当概率小于p时反转一个随机的变量
            //if (random.nextDouble() < p) {
                if (random.nextBoolean()) {
                    unsatisfiedClause.getVar1().setValue(!unsatisfiedClause.getVar1().getValue());
                } else {
                    unsatisfiedClause.getVar2().setValue(!unsatisfiedClause.getVar2().getValue());
                }
            /*} else {
                // Otherwise, flip the variable that maximizes the number of satisfied clauses
                int currentSatisfied = countSatisfiedClauses();
                unsatisfiedClause.getVar1().setValue(!unsatisfiedClause.getVar1().getValue());
                int satisfiedWithVar1Flipped = countSatisfiedClauses();
                unsatisfiedClause.getVar1().setValue(!unsatisfiedClause.getVar1().getValue()); // Flip back

                unsatisfiedClause.getVar2().setValue(!unsatisfiedClause.getVar2().getValue());
                int satisfiedWithVar2Flipped = countSatisfiedClauses();
                unsatisfiedClause.getVar2().setValue(!unsatisfiedClause.getVar2().getValue()); // Flip back

                if (satisfiedWithVar1Flipped > currentSatisfied) {
                    unsatisfiedClause.getVar1().setValue(!unsatisfiedClause.getVar1().getValue());
                } else if (satisfiedWithVar2Flipped > currentSatisfied) {
                    unsatisfiedClause.getVar2().setValue(!unsatisfiedClause.getVar2().getValue());
                } else {
                    if (random.nextBoolean()) {
                        unsatisfiedClause.getVar1().setValue(!unsatisfiedClause.getVar1().getValue());
                    } else {
                        unsatisfiedClause.getVar2().setValue(!unsatisfiedClause.getVar2().getValue());
                    }
                }
            }
        */}
        return false;
    }

    private int countSatisfiedClauses() {
        int count = 0;
        for (Clauses clause : clauses) {
            boolean aValue = clause.getVar1().getValue() == clause.getB1();
            boolean bValue = clause.getVar2().getValue() == clause.getB2();
            if (aValue || bValue) {
                count++;
            }
        }
        return count;
    }

   
}
