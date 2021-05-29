# count-with-gauge

```
gaugeCountViews.let {
	it.max = Random.nextInt(50, 100)
	it.count = Random.nextInt(0, it.max)
	it.backgroundLineColor = getRandomColor()
	it.defaultLineColor = getRandomColor()
	it.textSize = toPx(Random.nextInt(4, 9).toFloat())
	it.defaultTextColor = getRandomColor()
	it.warningBoundary = Random.nextInt(0, 20)
	it.warningLineColor = getRandomColor()
	it.warningTextColor = getRandomColor()
}
```

## Result
![image](https://raw.githubusercontent.com/seoft/android-laboratory/dev/count-with-gauge/art/art.gif)

