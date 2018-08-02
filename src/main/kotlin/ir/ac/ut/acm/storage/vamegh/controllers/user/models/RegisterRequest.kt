package ir.ac.ut.acm.storage.vamegh.controllers.user.models

data class RegisterRequest(
        val email: String,
        val bucketName: String,
        val password: String
) {
}