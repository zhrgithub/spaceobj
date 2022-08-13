package com.spaceobj.user.controller;

/**
 * @author zhr_java@163.com
 * @date 2022/8/7 20:17
 */
public class ListNode {

  int val;
  ListNode next;

  ListNode() {}

  ListNode(int val) {
    this.val = val;
  }

  ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
  }

  public static boolean isPalindrome(ListNode head) {
    StringBuffer str1 = new StringBuffer();

    ListNode newNode = head;
    while (newNode != null) {
      str1 = str1.append(newNode.val);
      newNode = newNode.next;
    }
    StringBuffer str2 = new StringBuffer(str1);
    str2.reverse();
    System.out.println(str1);

    System.out.println(str2);
    for (int i = 0; i < str1.length(); i++) {
      if (str1.charAt(i) != str2.charAt(i)) {
        return false;
      }
    }
    return true;
  }


  public static void main(String[] args) {
    ListNode list = new ListNode(1);
    list.next = new ListNode(2);
    boolean isPalindrome =  ListNode.isPalindrome(list);
    System.out.println(isPalindrome);
  }

}
