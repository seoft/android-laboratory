package kr.co.seoft.drag_and_drop_between_multiple_grid.model

enum class AppType(val intId: Int) {
    BASIC(0),
    EMPTY(1),
    FUNCTION(2),
    FOLDER(3),
    PHONE(4),
    SMS(5),
    SETTING(6)
}

open class ParentApp(open val appType: AppType, open val label: String)

data class BasicApp(
    override val label: String,
    val pkgName: String,
    override val appType: AppType = AppType.BASIC
) : ParentApp(appType, label)

data class EmptyApp(
    override val label: String,
    override val appType: AppType = AppType.EMPTY
) : ParentApp(appType, label)