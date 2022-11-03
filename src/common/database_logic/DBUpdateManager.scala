package common.database_logic

/**
 * @file: DBUpdateManager.scala
 * @description: Defines the methods for handling the user data update logic.
 * @author: KeepId Inc.
 * @date: October 31, 2022
 */

import common.constants.Domain

object DBUpdateManager {
  /**
   * updateUserData()
   * Updates the user's encrypted data stored within the Keep.
   * @param user_id The user whose encrypted data should be updated.
   * @param encrypted_data_fields The encrypted data fields to update the Keep with.
   * @return
   */
  def updateUserData(user_id: Domain.UserId, encrypted_data_fields: Domain.EncryptedDataFields): Boolean = {
    // TODO, complete the logic
  }
}
