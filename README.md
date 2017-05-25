# PagerIndicator
Custom view

![Picture](https://github.com/KavyrshinR/PagerIndicator/blob/master/gifsamples/device-2017-05-20-014535.gif)

# How to use

```java
PagerIndicator pagerIndicator = (PagerIndicator) findViewById(R.id.pagerIndicator);
viewPager.addOnPageChangeListener(pagerIndicator);
pagerIndicator.setCountIndicator(viewPager.getAdapter().getCount());
pagerIndicator.setCurrentPosition(viewPager.getCurrentItem());
```
