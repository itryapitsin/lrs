package com.arcusys.valamis.lrs.liferay.test.mysql

import com.arcusys.valamis.lrs.liferay.test.BaseCoreModule
import com.arcusys.valamis.lrs.test.config.MySqlDbInit

/**
 * Created by Iliya Tryapitsin on 10/01/15.
 */
object MySqlCoreModule extends BaseCoreModule(MySqlDbInit())