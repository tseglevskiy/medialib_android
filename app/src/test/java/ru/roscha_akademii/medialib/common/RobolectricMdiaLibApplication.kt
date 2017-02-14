package ru.roscha_akademii.medialib.common

class RobolectricMdiaLibApplication : MediaLibApplication() {
    fun setTestComponent(testComponent: ApplicationComponent)
    {
        _component = testComponent
    }
}

