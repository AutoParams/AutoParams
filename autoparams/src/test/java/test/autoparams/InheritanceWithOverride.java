package test.autoparams;

public class InheritanceWithOverride {

    public static class BaseWithOverridableSetter {
        private String value;
        private boolean setterCalled = false;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
            this.setterCalled = true;
        }

        public boolean isSetterCalled() {
            return setterCalled;
        }
    }

    public static class DerivedWithOverriddenSetter extends BaseWithOverridableSetter {
        private boolean derivedSetterCalled = false;

        @Override
        public void setValue(String value) {
            super.setValue(value);
            this.derivedSetterCalled = true;
        }

        public boolean isDerivedSetterCalled() {
            return derivedSetterCalled;
        }
    }
}
