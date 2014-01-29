# make all   :: to compile Java classes into CLASSDIR directory
# make jar   :: to create JAR file
# make clean :: to delete compiled classes and JAR file
# to run use one of the following commands:
# java -cp CLASSDIR PACKAGE.Main
# java -jar JARFILE
#>> end of help

# classes directory
CLASSDIR := classes/

# JAR directory
JARDIR := jar/

# output JAR file
JARFILE := $(JARDIR)YAJISudoku.jar

# package
PACKAGE := org.ailatovskiy.yajisudoku

# full package dir path
PACKAGEDIR := $(subst .,/,$(PACKAGE))/

# default target with short help message
HELP := help
.DEFAULT_GOAL := $(HELP)
.PHONY  : $(HELP)
$(HELP) :
	@grep -B 1000 -m 1 '^#>>' $(lastword $(MAKEFILE_LIST) ) | sed 's!CLASSDIR!$(CLASSDIR)!;s!PACKAGE!$(PACKAGE)!;s!JARFILE!$(JARFILE)!;s/^# //;$$d' | grep --color=always '.'

# Java source file ext
JAVA := .java

# Java class file ext
CLASS := .class

# Java compiler
JAVAC := javac

# Java compiler options
JAVACOPTS := -d $(CLASSDIR)

# Java source files
JAVASRC := $(wildcard *$(JAVA))

# Java class files
CLASSES := $(addprefix $(CLASSDIR)$(PACKAGEDIR),$(patsubst %$(JAVA),%$(CLASS),$(JAVASRC)))

# source icon file
ICONSRC := icon.png

# target icon file
ICON := $(CLASSDIR)$(PACKAGEDIR)$(ICONSRC)

# create classes directory, compile all Java sources, copy icon file
ALL := all
.PHONY : $(ALL)
$(ALL) : $(CLASSES) $(ICON) | $(CLASSDIR)

# create directories if needed
$(CLASSDIR) $(JARDIR) :
	@mkdir -pv -- $@

# target for the first class will produce all the classes because javac compiles all the sources simultaneously
FIRSTCLASS := $(firstword $(CLASSES))
$(FIRSTCLASS) : $(JAVASRC) | $(CLASSDIR)
	$(JAVAC) $(JAVACOPTS) $(JAVASRC)

# target for the remaining classes is just a stub to satisfy make all
$(filter-out $(FIRSTCLASS),$(CLASSES)) : | $(FIRSTCLASS)

$(ICON) : $(ICONSRC)
	@cp -pv -- $(ICONSRC) $(ICON)

# Manifest file
MANIFEST := Manifest.txt

JAR := jar
.PHONY   : $(JAR)
$(JAR) : $(JARFILE) | $(JARDIR)

$(JARFILE) : $(CLASSES) $(ICON) $(MANIFEST) | $(CLASSDIR) $(JARDIR)
	jar cvfm $(JARFILE) $(MANIFEST) -C $(CLASSDIR) .

DEL := -rm -vrf --
CLEAN := clean
.PHONY   : $(CLEAN)
$(CLEAN) :
	@$(DEL) $(CLASSDIR) $(JARDIR)

