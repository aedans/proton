Proton
======

An experimental structural and projectional text editor.

Why yet another editor
---------------------

Structural and projectional editing are the most powerful features that almost
all modern text editors and IDEs are lacking. While there are some tools which
support it, they are usually specialized for a specific domain and cannot be
extended outside of it. Proton is designed to be extensible, so it can be used 
on a day-to-day basis without needing to switch to another editor, while still
providing the advanced features that specialized structural editors provide.

So what are structural and projectional editing
-----------------------------------------------

Traditional text editors operate on 2-dimensional character arrays and provide
an extensive set of operations which allow the user to perform a variety of text 
transformations on a program. While this works for simple cases, it runs into 
several issues when editing large projects.

1. Advanced features such as auto-completion and auto-formatting have to be
   implemented using slow and complex algorithms to handle invalid code and
   incremental updates. As such, only the best tools for the most popular
   languages are able to support them quickly and correctly.
2. There is no layer between code storage and code editing, so programmers have
   to use whatever code style and syntax the project and language enforce rather 
   than what they prefer.

Structural editors solve the first issue by operating on arbitrary data
structures rather than being restricted to text buffers. This allows
language-level features to be faster and simpler since they can operate directly
on a program's tree rather than a textual representation of it.

Projectional editors solve the second issue by projecting the program's
structure onto a separate representation. Because it holds code as a data
structure rather than raw text, it can be edited however the user chooses
without changing the on-disk representation.

Combined, these techniques allow the programmer complete freedom when editing; C
can be edited as though it had the syntax of Python, Java can be extended with
data classes and extension functions, and all the functionality of a heavyweight
IDE can be provided with a fraction of the work.

Downsides
---------

While this all sounds good on paper, there are a few downsides that come with
it.

1. The editing model is different and takes a while to get used to. Though
   Proton tries to keep structural and textual editing as similar as possible,
   it's difficult to swap between the different styles, so the learning curve is
   steeper than a traditional editor.
2. The projection is automatically generated without human input. Anyone who's
   used an auto-formatter has run into a situation where it has formatted code
   in an ugly or unintuitive way, and the only way to fix it is by adding edge
   case rules to the projection.

Though these downsides (and others) certainly exist, they are easily outweighed 
by the benefits.

Using Proton
------------

Proton can be run easily by cloning this repository and building it with
gradle.

    git clone https://github.com/aedans/proton.git
    cd proton
    bash gradlew run

Once run, the in-editor documentation can be accessed by opening the
documentation folder.

Current State
-------------

Proton is currently very early in development and is unsuitable for use. There
is very little documentation, and pretty much everything is subject to change.
Feel free to poke around in the code, but don't expect things to be
configurable, fast, or even work like they're supposed to.

Contributing
------------

Since Proton is so early in development, contributing to it is probably not a
great idea. If you're interested in Proton feel free to star it and come back to
it later, but any contributions right now are likely to be outdated within a few
commits.
