/**
 * Copyright (C) 2011 Brian Ferris <bdferris@onebusaway.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.webapp.actions.admin;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.onebusaway.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@Results({@Result(type = "redirectAction", name = "success", params = {
    "actionName", "index"})})
public class DeleteStaleUsersAction extends ActionSupport {

  private static final long serialVersionUID = 1L;

  private UserService _userService;

  @Autowired
  public void setUserService(UserService userService) {
    _userService = userService;
  }

  @SkipValidation
  @Override
  public String execute() {
    _userService.deleteStaleUsers();
    return SUCCESS;
  }
}