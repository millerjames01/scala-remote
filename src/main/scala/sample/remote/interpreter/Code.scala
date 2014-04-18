package sample.remote.interpreter

final case class Code(toRun: String)

final case class CodeResult(result: String)