package org.openlrs.liferay

import com.liferay.portal.kernel.events.SimpleAction
import org.openlrs.RunningMode

/**
 * Created by Iliya Tryapitsin on 12.08.15.
 */
class LrsInitAction extends SimpleAction with LrsTypeLocator with LrsModeLocator {
  override def run(strings: Array[String]): Unit = {
    initLrsTypeSettings()
    initLrsModeSettings()

    RunningMode setCurrent getLrsMode
  }
}