Blockly.JavaScript['flash_mode'] = function (block) {
  var dropdown_mode = block.getFieldValue('mode');
  // TODO: Assemble JavaScript into code variable.
  var code = '(flash [' + dropdown_mode + '])';
  return code;
};

Blockly.JavaScript['camera_text_recognition'] = function (block) {
  var text_camera_text = block.getFieldValue('camera_text');
  // TODO: Assemble JavaScript into code variable.
  var code = '(is_recognize [' + text_camera_text + '])';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['camera_object_recognition'] = function (block) {
  var dropdown_recog_object_list = block.getFieldValue('recog_object_list');
  // TODO: Assemble JavaScript into code variable.
  var code = '(is_detect [' + dropdown_recog_object_list + '])';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['previous_story'] = function (block) {
  var number_prev_id = block.getFieldValue('PREV_ID');
  // TODO: Assemble JavaScript into code variable.
  var code = '(set_previous_story [' + number_prev_id + '])';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['next_story'] = function (block) {
  var number_next_id = block.getFieldValue('NEXT_ID');
  // TODO: Assemble JavaScript into code variable.
  var code = '(set_next_story [' + number_next_id + '])';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['ask_user'] = function(block) {
  var text_question = block.getFieldValue('question');
  // TODO: Assemble JavaScript into code variable.
  var code = '(question [' + text_question + '])';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['set_duration'] = function (block) {
  var number_duration = block.getFieldValue('duration');
  // TODO: Assemble JavaScript into code variable.
  var code = '(set_duration [' + number_duration + '])';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_NONE];
};

Blockly.JavaScript['get_duration'] = function(block) {
  var number_duration_from_id = block.getFieldValue('duration_from_id');
  // TODO: Assemble JavaScript into code variable.
  var code = '(get_duration [' + number_duration_from_id + '])';
  return code;
};

Blockly.JavaScript['start_block'] = function (block) {
  // TODO: Assemble JavaScript into code variable.
  var code = 'start';
  return code;
};

Blockly.JavaScript['end_block'] = function (block) {
  // TODO: Assemble JavaScript into code variable.
  var code = 'end';
  return code;
};