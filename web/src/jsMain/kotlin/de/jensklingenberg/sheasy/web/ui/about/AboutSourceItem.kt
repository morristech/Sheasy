package de.jensklingenberg.sheasy.web.ui.about

import components.materialui.IconButton
import components.materialui.ListItemIcon
import components.materialui.ListItemText
import components.materialui.icons.MoreVertIcon
import de.jensklingenberg.sheasy.web.components.materialui.List
import de.jensklingenberg.sheasy.web.components.materialui.ListItem
import de.jensklingenberg.sheasy.web.model.SourceItem
import de.jensklingenberg.sheasy.web.ui.common.styleProps
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RClass
import react.dom.div
import react.dom.p


interface OnFileClickListener {

}

data class AboutItem(
    val name: String,
    val subtitle: String,
    val icon: RClass<*>

)

class AboutSourceItem(
    val aboutItem: AboutItem,
    val itemClickFunction: ((Event) -> Unit?)? = null,
    val onMoreBtnClick: ((Event) -> Unit?)? = null
) :
    SourceItem() {

    override fun render(rBuilder: RBuilder) {

        with(rBuilder) {


                ListItem {
                    attrs {
                        // href = presenter.getFiles(it.path)
                        component = "a"
                        divider = true
                        styleProps(textAlign = "left")
                    }

                    ListItemIcon {
                        aboutItem.icon {}
                    }

                    ListItemText {

                            div {
                                +aboutItem.name
                                attrs {
                                    if (itemClickFunction != null) {
                                        onClickFunction = {itemClickFunction}//{ presenter.setPath(file.path) }

                                    }
                                }
                            }
                            p {
                                +aboutItem.subtitle
                                attrs {
                                    if (itemClickFunction != null) {
                                        onClickFunction = {itemClickFunction}//{ presenter.setPath(file.path) }

                                    }
                                }
                            }



                    }

                    onMoreBtnClick?.let {
                        IconButton {
                            MoreVertIcon {}
                            attrs {
                                asDynamic()["aria-owns"] = "simple-menu"
                                asDynamic()["aria-haspopup"] = true
                                onClick = {onMoreBtnClick}
                            }
                        }
                    }

                }


        }

    }

}