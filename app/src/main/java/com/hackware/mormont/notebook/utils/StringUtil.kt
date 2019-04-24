package com.hackware.mormont.notebook.utils


fun String.prepareString(range: Int): String {
    return if (this.length > range)
        this.substring(0, range).padEnd(range + 3, '.')
    else
        this
}