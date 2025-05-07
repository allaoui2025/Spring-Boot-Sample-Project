@PostMapping("/create")
public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {

    if (request.getUsername() == null || request.getUsername().isEmpty()) {
        throw new BadRequestException(Constants.MESSAGE_INVALIDUSERNAME);
    }

    if (request.getPassword() == null || request.getPassword().isEmpty()) {
        throw new BadRequestException(Constants.MESSAGE_INVALIDPASSWORD);
    }

    if (request.getTcno() == null || request.getTcno().length() != 11 || !Pattern.matches("\\d{11}", request.getTcno())) {
        throw new BadRequestException(Constants.MESSAGE_INVALIDTCNO);
    }

    if (userService.isUsernameExist(request.getUsername())) {
        throw new BadCredentialsException(Constants.MESSAGE_SAMEUSERNAMEEXIST);
    }

    if (userService.isTcnoExist(request.getTcno())) {
        throw new BadCredentialsException(Constants.MESSAGE_SAMETCNOEXIST);
    }

    User user = userService.createNewUser(new User(request.getUsername(), request.getPassword(), request.getTcno()));
    wealthService.newWealthRecord(user.getId());

    CreateUserResponse response = new CreateUserResponse();
    response.setUsername(user.getUsername());
    response.setTcno(user.getTcno());
    return response;
}
