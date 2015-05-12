# libgdx-demo-pax-britannica-scala

Pax Britannica, ported to Scala. A learning exercise for myself, and hopefully this can be useful to the community as well.

#### Setup

* uses the libgdx-scala plugin found [here](https://github.com/libgdx/libgdx/wiki/Using-libgdx-with-Scala)
* uses Scala's SBT build tool instead of Gradle. I've found it to work well with no hiccups.


### Development notes

As of May 11 2015, porting is complete. However there are undoubtedly lots of bugs created in the process. Also, I did some light refactoring based on a lot of repeated code in the Factory classes.

#### null states 

The Java code uses a lot of null checking in the Targeting.java class to see if we've found a valid target. Scala tends to frown upon null states. For now, I'm using Scala's None:Option[T] syntax to basically defeat the Scala null aversion and get back to the original game's approach. Later on, I could implement a different approach more akin to Scala philosophies.

#### uninitialized fields

Along those lines -- there is a lot of this pattern going on:
   * reserve space for a value that will be set in the constructor
   * initialize it to the Scala default value, an underscore, since Scala won't let us use uninitialized values
   * set it again once we hit the constructor.

 Again, this is pretty ugly in the Scala world. I'll make it better I promise :)

#### Libgdx Array class and ForEach

Looks like the Libgdx Array class doesn't play well with the Scala for implementation. So java foreach works, but in Scala I don't get an iterator. Probably have to tweak the libgdx Array class for Scala. Interesting project!

#### Casting

Looks like casting in Scala is pretty straightforward.

    (Bomber) ship

looks like

    ship.asInstanceOf[Bomber]

#### GameInstance and Resources are now Scala Objects

Scala doesn't support static methods as such. Instead, you declare a singleton class of type Object. Then all the methods of this class are static. Can't combine static and instance methods in one class.

So far, this has worked, at least for compilation. I had to remove all the .getInstance() calls, so it looks like this:

    GameInstance.getInstance().amethod()

is now

    GameInstance.amethod()



