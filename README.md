# Leetcode-native-debugging-tool
leetcode 项目自动化构建、自动化生成测试用例、自动化测试

## 项目简介

我们在刷 leetcode 的时候，经常会遇到的问题是：线上很难生成测试用例进行调试（尤其是生成数组等数据结构时，会花费大量时间来构建测试用例数据结构），线上编辑器反应迟缓、功能单一。针对上述问题，本插件实现了以下三点功能点：

1. 本地自动化生成 leetcode 题目文件
2. 基于极简语法快速自动化生成测试用例
3. 自动化测试代码

只需要手动编写测试文件，例如：

```java
// test.txt

public int[] pivotIndex(float[][] nums, int target) //函数名称
nums = [[1,7],[11, 15]], target = 9 // 测试用例的输入数据
[1,2,3] // 测试用例的输出数据
```

即可生成 Solution.java 题目文件

```java
// Solution.java

public class Solution {
	public int[] pivotIndex(float[][] nums, int target) {
		return null;
	}
}
```

并可在编写 Solution.java 文件后，根据测试用例测试代码：

```
// 测试输出

解答错误！
标准输出：[1,2,3]
我的输出：null
```

