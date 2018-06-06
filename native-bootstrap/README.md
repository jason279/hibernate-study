## Overview
This sample illustrates how to bootstrap hibernate in latest native model. 

The bootstrapping API is quite flexible, but in most cases it makes the most sense to think of it as a 3 step process.

### Step 1: Build the StandardServiceRegistry
In fact, before building StandardServiceRegistry, we should build a BootstarpServiceRegistry and pass it to the constructor of StandardServiceRegistry, but in most case we can use the default BootstarpServiceRegistry. 

The StandardServiceRegistry can be built via StandardServiceRegistryBuilder, and before build(), we can load the configuration via config() method.

```
StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()//
	.configure("hibernate.cfg.xml")//
	.build();
```

#### Step 2: Build the Metadata
Metadata can be built via MetadataSources' buildMetadata() method, the MetadataSources can load the mapping information (also can be import via the configuration file).

```
Metadata metadata = new MetadataSources(standardRegistry)//
	.addAnnotatedClass(Book.class)//
	.getMetadataBuilder().applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)//
	.build();
```

#### Step 3: Build the SessionFactory
The last step is build the SessionFactory from the Metadata's build() method:

```
SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
```
