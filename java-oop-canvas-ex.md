This exercise will focus on basic OOP and some polymorphism.

In the exercise, we implement a basic 2d drawing code for basic shapes, using the terminal as our canvas. Our Canvas has to be able to draw the shape onto the canvas at a specific position. Kind of like one will be able to do with a normal graphics framework. 

In order to be able to draw the shapes in specific positions, due to the limitations of the console, we must create a _buffer_ for our drawing, into which we will place the characters to display. This will allow us to place characters wherever we want. Then, we can print this buffer to the console and get what we want.

The project will be made up of 
- `Canvas`: which will do the actual drawing to the console
- Shapes: which will define properties for a shape and how to draw each
- Main: input and drawing of the shapes

We will follow a UML sketch to help us make these classes:

<img width="1101" height="749" alt="image" src="https://github.com/user-attachments/assets/d757bccf-5471-4112-a960-8d07e18a6546" />

This sketch indicates the classes we have, their members, constructors and methods of each, and the relationship between the classes.

*Note*, on coordinate system:
- The 0 (start) point is the top left corner of the console
- The y grows downwards, and the x rightwards
- width refers to the x axis, height to the y

##### Relevant Material 

- Classes ([w3schools](https://www.w3schools.com/java/java_classes.asp) [geeksforgeeks](https://www.geeksforgeeks.org/classes-objects-java/))
- Class Methods ([w3schools](https://www.w3schools.com/java/java_class_methods.asp) [geeksforgeeks](https://www.geeksforgeeks.org/methods-in-java/?ref=lbp))
- Access Modifiers ([geeksforgeeks](https://www.geeksforgeeks.org/access-modifiers-java/?ref=lbp))
- Constructors ([w3schools](https://www.w3schools.com/java/java_constructors.asp) [geeksforgeeks](https://www.geeksforgeeks.org/constructors-in-java/?ref=lbp))
- Inheritence ([w3schools](https://www.w3schools.com/java/java_inheritance.asp) [geeksforgeeks](https://www.geeksforgeeks.org/inheritance-in-java))
- Polymorphism ([w3schools](https://www.w3schools.com/java/java_polymorphism.asp) [geeksforgeeks](https://www.geeksforgeeks.org/polymorphism-in-java/))
- Abstraction ([w3schools](https://www.w3schools.com/java/java_abstract.asp) [geeksforgeeks](https://www.geeksforgeeks.org/abstraction-in-java-2))
- Interfaces ([w3schools](https://www.w3schools.com/java/java_interface.asp) [geeksforgeeks](https://www.geeksforgeeks.org/interfaces-in-java/))
- 1D Arrays ([w3schools](https://www.w3schools.com/java/java_arrays.asp) [geeksforgeeks](https://www.geeksforgeeks.org/arrays-in-java/))
- 2D Arrays ([w3schools](https://www.w3schools.com/java/java_arrays_multi.asp) [geeksforgeeks](https://www.geeksforgeeks.org/multidimensional-arrays-in-java/))
- ArrayList ([w3schools](https://www.w3schools.com/java/java_arraylist.asp))

##### Part 1

We will start by implementing the shapes. Each shape is an implementation of the `Shape` interface. Because shapes are so different, most of their content is shape-specific. The common is the `draw` method (from `Shape` interface), because all shapes needs to be drawn.

Create `Shape` interface and `Point` helper class:
- `Shape` is an interface which will represent all of our shapes. It has `draw(char[][])`, which will be called (later) to draw the shape onto our display array.
- `Point` will be a utility class the represents a point in 2D, so it has an `x` and an `y` member, provide them with `get`/`set` methods. We will use it to represent points in our shapes classes. 

Then, implement the shapes. For simplification, draw only straight through-row or through-column lines; that is, the line is not diagonal. Because of the need for diagonal lines, do not implement the `Triangle` class for now.
- For `Line` we have the `start` and `end` points of the line. Implement the `draw` method so that it places `*` in each array element where the lines goes through, from start of the line to the end of it. For line `start=(x=0, y=0), end=(x=0, y=2)` we will have a line that goes through `(0, 0), (0, 1), (0, 2)`.
- For `Rectangle` we have the `topLeft` and `bottomRight` points of the rectangle. Using them we can represent the entire rectangle, think on how to get the width and height of the rectangle from them. Implement the `draw` method so that it places `*` in each array element where the rectangle edges go through. So for rectangle `topLeft=(0, 0), bottomRight=(2, 2)` we have the edges `(0, 0) -> (0, 2), (0, 2) -> (2, 2), (2, 2) -> (2, 0), (2, 0) -> (0, 0)`. Each edge can be drawn as a line is drawn since they are basically lines.

**Bonus**, on drawing diagonal lines: see [DDA algorithm](https://www.geeksforgeeks.org/dda-line-generation-algorithm-computer-graphics/). Also, make sure the slope for the line is 1 or -1, as anything else won't be shown properly on the canvas. Once this is finished, you can implement the `Triangle` class.

##### Part 2

Before starting, make sure you are familiar with the basics of [`ArrayList`](https://www.w3schools.com/java/java_arraylist.asp). For our purposes, we will use `ArrayList<Shape>`, which is a list of `Shape` instances. Becaues all of our shapes inherit `Shape` class, this list can contain all the different types.

Then implement the `Canvas` class, so that we can add shapes to it, and that `draw` will draw all the added shapes. The difficulty of drawing the shapes is that in canvas, we can't write to a specific point, we can only write line by line. So to do this, we will write the shapes data into an array and then print the array.
- `addShape` will receive a shape and add it to the list of shapes
- `draw` will create our drawing array, iterate over all the shapes in the list and call `shape.draw` for each and then print the contents of the array.

Experiment with this `Canvas` class in `main` by creating several shapes, adding them to the canvas and drawing them. See that it draws them correctly. Create shapes at different positions and add them to the `Canvas`. Afterwards, draw them by calling `canvas.draw()`.

##### Part 3

To make it more interesting, we can make all the shapes move. In order to do this, we need to apply a _velocity_ to the shapes, we will make them change position (on either axis) between every draw.

There is no UML for this, everyone will have to come up with their own solutions on how to implement this. But the basis is: each shape should have a field indicating velocity for each axis. Upon (or before) each draw, the shape will updates its coordinate according to this velocity. So for a point x, with velocity 1, the next position will be x+1.
- Each shape will need information about whats its current velocity is. This is required so that we can update the shape position by this velocity value. There should be one velocity value for each axis (x and y). Make sure there are getters and setters. This can be done with inheritence.
- Add a `update` method to each shape (must be also defined in the `Shape` interface. Think on why though). In this method, update the position of each shape by the velocity value by changing the points of each shape. For example, if the line has an `x` velocity of `1`, and a start point of `(1, 3)`, then following an update the new point should be `(2, 3)`. That way, the next call to `draw` will draw the shape in the new position.
- Update `Canvas` so that `draw` updates the position of each shape by calling `shape.update`.

In the main, create a few shapes and add them to a `Canvas` instance. Make a loop which draws every second:
```java
while (true) {
	canvas.draw();

	// this will make the pc wait for a second beforee continuing. 
	try {
		Thread.sleep(1000);
	} catch(InterruptedException e) {
		break;
	}
}
```

##### Post Exercise Questions

- Why did we create the Point class?
- Why did we use the Shape interface
- Why did you tell us what classes and methods to write to write?
- Is there another solution to console canvas drawing?
- Why did we do this solution and not another?
- So when should we use inheritence?
