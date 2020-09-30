package ipl.isel.daw.group8.common

const val HOSTNAME = "http://localhost:8080"

const val ROOT_PATH = "/api/"

const val USERS_LIST_PATH = "${ROOT_PATH}users"
const val USERS_PROJECTS_LIST_PATH = "${USERS_LIST_PATH}/projects"

const val USER_PATH = "${ROOT_PATH}{user_id}"

const val USER_PROJECTS_LIST_PATH = "${USER_PATH}/projects"
const val USER_PROJECT_PATH = "${USER_PATH}/{project_id}"

const val USER_PROJECT_ISSUES_LIST_PATH = "${USER_PROJECT_PATH}/issues"
const val USER_PROJECT_ISSUE_PATH = "${USER_PROJECT_PATH}/{issue_id}"

const val USER_PROJECT_ISSUE_COMMENTS_LIST_PATH = "${USER_PROJECT_ISSUE_PATH}/comments"
const val USER_PROJECT_ISSUE_COMMENT_PATH = "${USER_PROJECT_ISSUE_PATH}/{comment_id}"