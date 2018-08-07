package net.cnki.odatax.exception;

/**
 * @author hudianwei
 * @date 2018/8/2 14:54
 */
/*Throwable是java.lang包中一个专门用来处理异常的类。它有两个子类，即Error 和Exception，它们分别用来处理两组异常。
Error用来处理程序运行环境方面的异常，比如，虚拟机错误、装载错误和连接错误，这类异常主要是和硬件有关的，而不是由程序本身抛出的。
Exception是Throwable的一个主要子类。Exception下面还有子类，其中一部分子类分别对应于Java程序运行时常常遇到的各种异常的处理，
其中包括隐式异常。比如，程序中除数为0引起的错误、数组下标越界错误等，这类异常也称为运行时异常，因为它们虽然是由程序本身引起的异常，
但不是程序主动抛出的，而是在程序运行中产生的。
Exception 子类下面的另一部分子类对应于Java程序中的非运行时异常的处理（在下图中将它们直接属于Exception了），这些异常也称为显式异常。
它们都是在程序中用语句抛出、并且也是用语句进行捕获的，比如，文件没找到引起的异常、类没找到引起的异常等。
一些主要子类对应的异常处理功能简要说明如下：
ArithmeticException——由于除数为0引起的异常；
ArrayStoreException——由于数组存储空间不够引起的异常；
ClassCastException—一当把一个对象归为某个类，但实际上此对象并不是由这个类 创建的，也不是其子类创建的，则会引起异常；
IllegalMonitorStateException——监控器状态出错引起的异常；
NegativeArraySizeException—一数组长度是负数，则产生异常；
NullPointerException—一程序试图访问一个空的数组中的元素或访问空的对象中的 方法或变量时产生异常；
OutofMemoryException——用new语句创建对象时，如系统无法为其分配内存空 间则产生异常；
SecurityException——由于访问了不应访问的指针，使安全性出问题而引起异常；
IndexOutOfBoundsExcention——由于数组下标越界或字符串访问越界引起异常；
IOException——由于文件未找到、未打开或者I/O操作不能进行而引起异常；
ClassNotFoundException——未找到指定名字的类或接口引起异常；
CloneNotSupportedException——一程序中的一个对象引用Object类的clone方法，但 此对象并没有连接Cloneable接口，从而引起异常；
InterruptedException—一当一个线程处于等待状态时，另一个线程中断此线程，从 而引起异常，有关线程的内容，将在下一章讲述；
NoSuchMethodException一所调用的方法未找到，引起异常；
Illega1AccessExcePtion—一试图访问一个非public方法；
StringIndexOutOfBoundsException——访问字符串序号越界，引起异常；
ArrayIdexOutOfBoundsException—一访问数组元素下标越界，引起异常；
NumberFormatException——字符的UTF代码数据格式有错引起异常；
IllegalThreadException—一线程调用某个方法而所处状态不适当，引起异常；
FileNotFoundException——未找到指定文件引起异常；
EOFException——未完成输入操作即遇文件结束引起异常。
Error类和Exception类的父类都是throwable类，他们的区别是：
Error类一般是指与虚拟机相关的问题，如系统崩溃，虚拟机错误，内存空间不足，方法调用栈溢等。对于这类错误的导致的应用程序中断，仅靠程序本身无法恢复和和预防，
遇到这样的错误，建议让程序终止。
Exception类表示程序可以处理的异常，可以捕获且可能恢复。遇到这类异常，应该尽可能处理异常，使程序恢复运行，而不应该随意终止异常。
Exception类又分为运行时异常（Runtime Exception）和受检查的异常(Checked Exception )，运行时异常;ArithmaticException,IllegalArgumentException，编译能通过，
但是一运行就终止了，程序不会处理运行时异常，出现这类异常，程序会终止。而受检查的异常，要么用try。。。catch捕获，要么用throws字句声明抛出，交给它的父类处理，
否则编译不会通过。
*/
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    protected int code = 500;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return this.code;
    }
}
