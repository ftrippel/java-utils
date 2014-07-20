package ftrippel.utils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author https://github.com/ftrippel
 * 
 */
public class BeanUtilsTest {

	public static class C {

	};

	public static class B {
		private C c;

		public C getC() {
			return c;
		}

		public void setC(C c) {
			this.c = c;
		}

	};

	public static class A {
		private B b;

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

	};

	@Test
	public void ensureNotNull() {
		A a = new A();
		Assert.assertNull(a.getB());

		BeanUtils.ensureBeanPropertyNotNull(a, "b");
		Assert.assertNotNull(a.getB());

		BeanUtils.ensureBeanPropertyNotNull(a, "b.c");
		Assert.assertNotNull(a.getB().getC());
	}

	@Test
	public void setBeanProperty() {
		A a = new A();
		Assert.assertNull(a.getB());

		BeanUtils.setBeanProperty(a, "b", new B());
		Assert.assertNotNull(a.getB());

		BeanUtils.setBeanProperty(a, "b.c", new C());
		Assert.assertNotNull(a.getB().getC());
	}

	public static class DeepCloneA implements Serializable {
		public DeepCloneB b1, b2;
	}

	public static class DeepCloneB implements Serializable {
	}

	@Test
	public void deepClone() {
		DeepCloneA a = new DeepCloneA();
		DeepCloneB b = new DeepCloneB();
		a.b1 = b;
		a.b2 = b;
		Assert.assertTrue(!a.b1.equals(new DeepCloneB()));
		Assert.assertTrue(a.b1.equals(a.b2));
		DeepCloneA a2 = (DeepCloneA) BeanUtils.deepClone(a);
		Assert.assertTrue(a.b1.equals(a.b2));
		Assert.assertTrue(!a.b1.equals(a2.b1));
	}

	public static class BeanEquals {

		public String erstesProperty;

		public String zweitesProperty;

		public String drittesProperty;

		public String pProperty;

		public String keinProperty; // hat keine getter und setter methoden

		public String getErstesProperty() {
			return erstesProperty;
		}

		public void setErstesProperty(String erstesProperty) {
			this.erstesProperty = erstesProperty;
		}

		public String getZweitesProperty() {
			return zweitesProperty;
		}

		public void setZweitesProperty(String zweitesProperty) {
			this.zweitesProperty = zweitesProperty;
		}

		public String getDrittesProperty() {
			return drittesProperty;
		}

		public void setDrittesProperty(String drittesProperty) {
			this.drittesProperty = drittesProperty;
		}

		public String getPProperty() {
			return pProperty;
		}

		public void setPProperty(String property) {
			pProperty = property;
		}

	}

	@Test
	public void assertBeanEqualsTest1() {
		BeanEquals a = new BeanEquals(), b = new BeanEquals();
		a.erstesProperty = "1";
		b.erstesProperty = "1";
		BeanUtils.assertBeanEqualsExcept(a, b, Collections.EMPTY_LIST);
	}

	@Test
	public void assertBeanEqualsTest2() {
		BeanEquals a = new BeanEquals(), b = new BeanEquals();
		a.keinProperty = "1";
		b.keinProperty = "2";
		BeanUtils.assertBeanEqualsExcept(a, b, Collections.EMPTY_LIST);
	}

	@Test(expected = RuntimeException.class)
	public void assertBeanEqualsTest3() {
		BeanEquals a = new BeanEquals(), b = new BeanEquals();
		a.erstesProperty = "1";
		b.erstesProperty = "2";
		BeanUtils.assertBeanEqualsExcept(a, b, Collections.EMPTY_LIST);
	}

	@Test
	public void assertBeanEqualsTest4() {
		BeanEquals a = new BeanEquals(), b = new BeanEquals();
		a.erstesProperty = "1";
		b.erstesProperty = "2";
		a.pProperty = "1";
		a.pProperty = "2";
		BeanUtils.assertBeanEqualsExcept(a, b, Arrays.asList("erstesProperty", "PProperty"));
	}

	public static class ReducibleBeanA {
		public Integer j;

		public Integer getJ() {
			return j;
		}

		public void setJ(Integer j) {
			this.j = j;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((j == null) ? 0 : j.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ReducibleBeanA other = (ReducibleBeanA) obj;
			if (j == null) {
				if (other.j != null)
					return false;
			} else if (!j.equals(other.j))
				return false;
			return true;
		}

	}

	public static class ReducibleBeanB {
		public ReducibleBeanA bean;

		public Integer i;

		public ReducibleBeanA getBean() {
			return bean;
		}

		public void setBean(ReducibleBeanA bean) {
			this.bean = bean;
		}

		public Integer getI() {
			return i;
		}

		public void setI(Integer i) {
			this.i = i;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bean == null) ? 0 : bean.hashCode());
			result = prime * result + ((i == null) ? 0 : i.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ReducibleBeanB other = (ReducibleBeanB) obj;
			if (bean == null) {
				if (other.bean != null)
					return false;
			} else if (!bean.equals(other.bean))
				return false;
			if (i == null) {
				if (other.i != null)
					return false;
			} else if (!i.equals(other.i))
				return false;
			return true;
		}

	}

	@Test
	public void assertReducibleBean() {
		ReducibleBeanB bean1 = new ReducibleBeanB();
		bean1.i = 1;
		bean1.bean = new ReducibleBeanA();
		bean1.bean.j = 1;

		ReducibleBeanB bean2 = new ReducibleBeanB();
		bean2.i = 2;
		bean2.bean = new ReducibleBeanA();
		bean2.bean.j = 2;

		BeanUtils.reduceBeans(bean1, bean1, new BeanUtils.BeanReducer() {
			public Object reduce(Object o1, Object o2) {
				if (o2 instanceof Integer) {
					if (o1 == null) {
						o1 = new Integer(0);
					}
					return ((Integer) o1) + ((Integer) o2);
				} else {
					throw new IllegalArgumentException();
				}
			}
		}, Arrays.asList((Class) Integer.class));
		BeanUtils.assertBeanEqualsExcept(bean1, bean2, Collections.EMPTY_LIST);
	}

}
