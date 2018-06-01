## Overview
This sample illustrates how to bootstrap hibernate in legacy native model. 
> The legacy way to bootstrap a SessionFactory is via the org.hibernate.cfg.Configuration object. Configuration represents, essentially, a single point for specifying all aspects of building the SessionFactory: everything from settings, to mappings, to strategies, etc. I like to think of Configuration as a big pot to which we add a bunch of stuff (mappings, settings, etc) and from which we eventually get a SessionFactory.
> -- Hibernate Document


#### Note:
- Don't recommend native bootstrap style, if must to use, we'd better use latest native bootstrapping which the APIs is redesigned in 5.0.