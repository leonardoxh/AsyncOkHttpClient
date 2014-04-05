Pull Requests
=================
Well if you is seeing this and wanna contribute is a good point, all pull request are welcome but before submit any pull request please review your code and test it, all pull requests will be tested by me, and think in a real use case for the pull request, I **will not** merge a pull request without a motive.

But if this ok I have some roles and you need follow them, basically is the Android team code style with some adaptations is really easy, this is necessary, like beer:
    
1. Line tab identation is 4 spaces
2. Line continuation is 8 spaces
3. Prefrer the <code>foreach</code> statment before normal <code>for</code>
4. <code>if</code> is best then <code>try... catch</code>

If statments:
---
**Correct**
```
if(1 == 1) {
    //multiple line if statment
}

if(1 == 1) System.out.println("This if is ok");
```
**Incorrect**
```
if(1 == 1)
    System.out.println("oh s**it");
```

And this is so important to, avoid put a blank lines on your code example:
**correct**
```
void method() {
    System.out.println("OK");
    System.out.println("Another line goes here");
}
```
**Incorrect**
```
void method() {
    System.out.println("This...");
    
    System.out.println("IS NOT CORRECT HERE");
}
```
Methods must be separated by 1 blank line its easy:
```
void method() {
    //code...
}

void method2() {
    //code...
}
```
Methods without code, this way:
```
void method() { }
```
Variable cast:
```
String a = (String)obj;
```
