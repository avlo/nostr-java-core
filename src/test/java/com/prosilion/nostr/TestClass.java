package com.prosilion.nostr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestClass {

  public static void main(String[] args) {
    method3();
  }

  public static void method2() {
    List<Number> numbers = new ArrayList<>();
    numbers.add(5);
    numbers.add(10L);
    numbers.add(15f);
    numbers.add(20.0);
    long sum = sum(numbers);
    System.out.println(sum);

    List<Long> longs = new ArrayList<>();
    longs.add(5L);
    longs.add(10L);
    longs.add(15L);
//    integers.add(20.0);
//    sum(longs);    // <------------- won't work
  }

  public static long sum(List<Number> numbers) {
    return numbers.stream().mapToLong(Number::longValue).sum();
  }

//  -------------------------------
//  -------------------------------  

  public static long sumWildcard(List<? extends Number> numbers) {
    return numbers.stream().mapToLong(Number::longValue).sum();
  }
  
  public static void method3() {
    List<Integer> integers = new ArrayList<>();
    integers.add(5);
    integers.add(10);
    long l = sumWildcard(integers);
    System.out.println(l);
  }

//  -------------------------------
//  -------------------------------

  void method1() {
    List<Number> numbers1 = new ArrayList<>();
    numbers1.add(5);
    numbers1.add(10L);

    List<Number> numbers2 = new ArrayList<>();
    numbers2.add(15f);
    numbers2.add(20.0);

//    List<Number> numbersMergedWildcard = bad(numbers1, numbers2);
    List<Number> numbersMergedType = good(numbers1, numbers2);
    List<Number> numbersMergedOption = option(numbers1, numbers2);
  }

  <E> List<? extends E> bad(
      List<? extends E> listOne,
      List<? extends E> listTwo) {
    return get(listOne, listTwo);
  }

  <E> List<E> good(
      List<? extends E> listOne,
      List<? extends E> listTwo) {
    return get(listOne, listTwo);
  }

  <E, T extends E> List<E> option(
      List<T> listOne,
      List<T> listTwo) {
    return get(listOne, listTwo);
  }

  private static <E> List<E> get(List<? extends E> listOne, List<? extends E> listTwo) {
    return Stream.concat(listOne.stream(), listTwo.stream())
        .collect(Collectors.toList());
  }
}
