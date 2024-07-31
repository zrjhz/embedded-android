//package im.zhy.data.arithmetic;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author zhy
// * @create_date 2019-04-28 9:31
// */
//public class MathUtils {
//
//    // 大数阶乘取余
//    public static long power(long x, long n, long p) {
//        if(n == 0)
//            return 1;
//
//        long tmp = 1;
//        for(long i = 0; i < n; i++)
//        {
//            tmp = (x * tmp) % p;
//        }
//        return tmp;
//    }
//
//    // 大数相加取余
//    public static long adder(long number1, long number2, long n){
//        return ((number1 % n) + (number2 % n)) % n;
//    }
//
//    // 大数相乘取余
//    public static long ride(long number1, long number2, long n){
//        return ((number1 % n) * (number2 % n)) % n;
//    }
//
//    // 判断一个是否是素数（质数）
//    public static boolean isPrime(long number){
//        if (number <= 0){
//            return false;
//        }
//
//        for (long i = 2; i < number; i++){
//            if (number % i == 0){
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    // 循环法求最大公约数
//    public static int maxCommonDivisor(int m, int n) {
//
//        if (m < n) {// 保证m>n,若m<n,则进行数据交换
//            int temp = m;
//            m = n;
//            n = temp;
//        }
//        while (m % n != 0) {// 在余数不能为0时,进行循环
//            int temp = m % n;
//            m = n;
//            n = temp;
//        }
//        return n;// 返回最大公约数
//    }
//
//    // 求最小公倍数
//    public static int minCommonMultiple(int m, int n) {
//        return m * n / maxCommonDivisor(m, n);
//    }
//
//
//
//    // 两数是否互质
//    public static boolean coprimeNumber(int a, int b){
//        if(a < b) {
//            int tmp = a;
//            a = b;
//            b = tmp;
//        }
//        int c;
//        while((c = a % b) != 0) {
//            a = b;
//            b = c;
//        }
//
//        return b == 1 ? true : false;
//    }
//
//
//    // 返回这个数的互质数集合
//    public static List<Integer> getCoprimeNumberList(int number){
//
//        List<Integer> result = new ArrayList<>();
//
//        for (int i = 2; i < number; i++) {
//
//            if (coprimeNumber(number, i)) {
//                result.add(i);
//            }
//
//        }
//
//        return result;
//
//    }
//
//
//    /**
//     * 求偶数和或奇数和
//     * @param number
//     * @param isEven 偶数为true 奇数为 false
//     * @return
//     */
//    public static int getEvenOrOddNumberCount(List<Integer> number, boolean isEven){
//
//        int evenNumberCount = 0;
//        int oddNumberCount = 0;
//
//        for (Integer integer : number) {
//            if (integer % 2 == 0){
//                evenNumberCount += integer;
//            }else{
//                oddNumberCount += integer;
//            }
//        }
//
//        if (isEven){
//            return evenNumberCount;
//        }else {
//            return oddNumberCount;
//        }
//
//    }
//
//    /**
//     * 判断是否为偶数
//     * @param number
//     * @return
//     */
//    public static boolean isEvenNumber(int number){
//        if (number % 2 == 0){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//
//    public static boolean isOddNumber(int number){
//        if (number % 2 != 0){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//
//
//
//    public static void main(String[] args) {
//
//        System.out.println(isEvenNumber(2));
//        System.out.println(isOddNumber(2));
//
//    }
//
//
//}
