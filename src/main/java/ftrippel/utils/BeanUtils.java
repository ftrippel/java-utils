package ftrippel.utils;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author ftrippel
 * 
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

	public static String beanToString(Object bean) {
		return StringUtils.indentString(ToStringBuilder.reflectionToString(bean, new RecursiveToStringStyle()), '[', ',', ']');
	}

	public static Object deepClone(Serializable o) {
		return deserialize(serialize(o));
	}

	/** @author Found this one on StackOverflow */
	public static byte[] serialize(Serializable o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** @author Found this one on StackOverflow */
	public static Object deserialize(byte[] bytes) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static protected PropertyUtilsBean accessor = new PropertyUtilsBean();

	public static Object getBeanProperty(Object o, String path) {
		try {
			return accessor.getSimpleProperty(o, path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T getBeanProperty(Object o, String path, Class<T> klazz) {
		try {
			return (T) accessor.getSimpleProperty(o, path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setBeanProperty(Object o, String path, Object v) {
		try {
			String[] names = Pattern.compile(".", Pattern.LITERAL).split(path);
			int i = 1, j = names.length;
			for (String name : names) {
				if (i == j) {
					accessor.setSimpleProperty(o, name, v);
					break;
				}
				Object o2 = accessor.getSimpleProperty(o, name);
				if (o2 == null) {
					Method m = accessor.getPropertyDescriptor(o, name).getWriteMethod();
					String className = m.getGenericParameterTypes()[0].toString();
					if (className.startsWith("class ")) {
						className = className.substring(6);
					}
					Class c = o.getClass().getClassLoader().loadClass(className);
					o2 = c.getDeclaredConstructor().newInstance();
					accessor.setSimpleProperty(o, name, o2);

				}
				o = o2;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * The method initializes a path using no-argument constructors
	 */
	public static void ensureBeanPropertyNotNull(Object o, String path) {
		try {
			String[] names = Pattern.compile(".", Pattern.LITERAL).split(path);
			for (String name : names) {
				Object o2 = accessor.getSimpleProperty(o, name);
				if (o2 == null) {
					Method m = accessor.getPropertyDescriptor(o, name).getWriteMethod();
					String className = m.getGenericParameterTypes()[0].toString();
					if (className.startsWith("class ")) {
						className = className.substring(6);
					}
					Class c = o.getClass().getClassLoader().loadClass(className);
					o2 = c.getDeclaredConstructor().newInstance();
					accessor.setSimpleProperty(o, name, o2);

				}
				o = o2;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Compares bean properties first-level-wise using their equals-method
	 */

	public static void assertBeanEqualsExcept(Object bean1, Object bean2, List<String> except) {
		List<BeanDiff> diffs = beanDiff(bean1, bean2, except);

		if (diffs.size() == 0) {
			return;
		}
		String propertyName = diffs.get(0).property;
		Object property1 = diffs.get(0).value1;
		Object property2 = diffs.get(0).value2;

		if ((property1 != null && property2 == null) || (property1 == null && property2 != null) || !property1.equals(property2)) {
			throw new RuntimeException("Beans not equal: property=" + propertyName + ", value1=" + property1 + ", value2=" + property2 + ", bean1="
					+ beanToString(bean1) + ", bean2=" + beanToString(bean2));
		}
	}

	public static boolean isBeanEqualsExcept(Object bean1, Object bean2, List<String> except) {
		List<BeanDiff> diffs = beanDiff(bean1, bean2, except);
		return diffs.size() == 0;
	}

	public static class BeanDiff {
		public String property;

		public Object value1, value2;

		public BeanDiff(String property, Object value1, Object value2) {
			super();
			this.property = property;
			this.value1 = value1;
			this.value2 = value2;
		}
	};

	public static List<BeanDiff> beanDiff(Object bean1, Object bean2, List<String> except) {
		List<BeanDiff> diffs = new ArrayList<BeanDiff>();
		PropertyUtilsBean propUtils = accessor;
		BeanMap map = new BeanMap(bean1);
		for (Object propNameObject : map.keySet()) {
			String propertyName = (String) propNameObject;
			if (propertyName.equals("class")) {
				continue;
			}
			if (!except.contains(propertyName)) {
				Object property1 = null, property2 = null;
				try {
					property1 = propUtils.getProperty(bean1, propertyName);
					property2 = propUtils.getProperty(bean2, propertyName);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (property1 == null && property2 == null) {
					continue;
				}
				if ((property1 != null && property2 == null) || (property1 == null && property2 != null) || !property1.equals(property2)) {
					diffs.add(new BeanDiff(propertyName, property1, property2));
				}
			}
		}
		return diffs;
	}

	/**
	 * Given bean1 and bean2 the method reduces (merges) bean2 into bean1 according to predefined property classes (types) and a reducer function.
	 * 
	 * Properties of bean2 are recursively walked through.
	 * 
	 * If a the corresponding property in bean1 is null no-argument construction is tried.
	 * 
	 */

	public interface BeanReducer {
		Object reduce(Object o1, Object o2);
	}

	final public static void reduceBeans(Object o1, Object o2, BeanReducer reducer, List<Class> reduceAt) {
		try {
			Set<Class> reduceAtSet = new HashSet<Class>();
			reduceAtSet.addAll(reduceAt);
			Stack<Object> stack_o1 = new Stack<Object>();
			Stack<Object> stack_o2 = new Stack<Object>();
			stack_o1.push(o1);
			stack_o2.push(o2);
			while (!stack_o2.isEmpty()) {
				Object _o1 = stack_o1.pop();
				Object _o2 = stack_o2.pop();
				PropertyDescriptor[] props = accessor.getPropertyDescriptors(_o2);
				for (PropertyDescriptor prop : props) {
					if (prop.getName().equals("class") || prop.getReadMethod() == null || prop.getWriteMethod() == null) {
						continue;
					}
					Object val_o1 = accessor.getSimpleProperty(_o1, prop.getName());
					Object val_o2 = accessor.getSimpleProperty(_o2, prop.getName());
					if (val_o2 != null) {
						Method m = prop.getWriteMethod();
						if (val_o1 == null) {
							String className = m.getGenericParameterTypes()[0].toString();
							if (className.startsWith("class ")) {
								className = className.substring(6);
							}
							Class c = o1.getClass().getClassLoader().loadClass(className);
							try {
								val_o1 = c.getDeclaredConstructor().newInstance();
								accessor.setSimpleProperty(_o1, prop.getName(), val_o1);
							} catch (NoSuchMethodException e) {
							} catch (IllegalAccessException e) {
							}
						}
						if (reduceAtSet.contains(val_o2.getClass())) {
							accessor.setSimpleProperty(_o1, prop.getName(), reducer.reduce(val_o1, val_o2));
						} else {
							stack_o1.push(val_o1);
							stack_o2.push(val_o2);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
