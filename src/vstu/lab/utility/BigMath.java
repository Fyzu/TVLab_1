package vstu.lab.utility;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * TVLab_1 created by Dmitry on 19.02.2016.
 */
public class BigMath {

    /* factorial code */
    public static BigInteger factorial(int n) {
        if(n < 0)
            return BigInteger.ZERO;
        if(n == 0)
            return BigInteger.ONE;
        if(n == 1 || n == 2)
            return BigInteger.valueOf(n);
        return ProdTree(2, n);
    }
    public static BigInteger ProdTree(int l, int r) {
        if(l > r)
            return BigInteger.ONE;
        if(l==r)
            return BigInteger.valueOf(l);
        if(r-1 == 1)
            return BigInteger.valueOf(l * r);
        int m = (l+r)/2;
        return ProdTree(l, m).multiply(ProdTree(m + 1, r));
    }

    /**
     * Класс комбинаторики из раздела теории вероятности и мат. статистики
     */
    public static class Combinatorics {

        /** Кол-во перестановок объектов без повторений
         *  "Сколькими способами можно переставить n объектов?"
         * @param n - кол-во объектов
         * @return - кол-во перестановок
         */
        public static BigInteger permutations(int n) {

            return factorial(n);
        }

        /** Кол-во перестановок объектов с повторениями
         * @param numbers - массив кол-ва повторяющихся объектов
         * @return - кол-во перестановок
         */
        public static BigInteger permutationsRepeat(ArrayList<Integer> numbers) {

            BigInteger result = BigInteger.ZERO;
            int i = 0;
            try {
                for(int number : numbers) {
                    i += number;
                }
                result = factorial(i);
                for(int number : numbers) {
                    result = result.divide(factorial(number));
                }
            } catch (ArithmeticException e) {
                e.printStackTrace();
            }
            return result;
        }

        /** Кол-во размещений объектов без повторений
         * "Сколькими способами можно выбрать m объектов из n объектов и в каждой выборке переставить их местами (либо распределить между ними какие-то атрибуты)?"
         * @param n - общее кол-во объектов
         * @param m - общее кол-во объектов
         * @return - кол-во размещений
         */
        public static BigInteger placement(int n, int m) {

            return factorial(n).divide(factorial(n-m));
        }

        /** Кол-во размещений объектов c повторениями
         * "Сколькими способами можно выбрать m объектов из n объектов и в каждой выборке переставить их местами (либо распределить между ними какие-то атрибуты)?"
         * @param n - общее кол-во объектов
         * @param m - общее кол-во объектов
         * @return - кол-во размещений
         */
        public static BigInteger placementRepeat(int n, int m) {

            return BigInteger.valueOf(n).pow(m);
        }

        /** Кол-во сочетаний объектов без повторений
         * "Сколькими способами можно выбрать m объектов из n объектов?"
         * @param n - общее кол-во объектов
         * @param m - общее кол-во объектов для сочетаний
         * @return - кол-во сочетаний
         */
        public static BigInteger combinations(int n, int m) {

            return factorial(n).divide(factorial(m)).divide(factorial(n-m));
        }

        /** Кол-во сочетаний объектов с повторениями
         * "Для выбора предложено n множеств одинаковых объектов. Сколькими способами можно взять m объектов?"
         * @param n - общее кол-во объектов
         * @param m - общее кол-во объектов для сочетаний
         * @return - кол-во сочетаний
         */
        public static BigInteger combinationsRepeat(int n, int m) {

            return combinations(n+m-1, m);
        }
    }
}
