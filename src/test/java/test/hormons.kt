// Hormone base class with decay and concentration
abstract class Hormone(val name: String, var concentration: Double) {
    abstract fun affect(organ: Organ)

    open fun decay(): Boolean {
        concentration *= 0.8  // 20% decay
        return concentration > 0.1
    }
}

// Specific hormones
class Adrenaline(conc: Double = 1.0) : Hormone("Adrenaline", conc) {
    override fun affect(organ: Organ) {
        if ("Adrenaline" in organ.receptors) {
            when (organ.name) {
                "Lungs" -> organ.state = "Breathing increased"
                "Stomach" -> organ.state = "Digestion slowed"
                "Heart" -> organ.state = "Heart rate increased"
            }
        }
    }
}

class Dopamine(conc: Double = 1.0) : Hormone("Dopamine", conc) {
    override fun affect(organ: Organ) {
        if ("Dopamine" in organ.receptors && organ.name == "Brain") {
            organ.state = "Happy"
        }
    }
}

class Melatonin(conc: Double = 1.0) : Hormone("Melatonin", conc) {
    override fun affect(organ: Organ) {
        if ("Melatonin" in organ.receptors) {
            when (organ.name) {
                "Brain" -> organ.state = "Sleepy"
                "Eyes" -> organ.state = "Adjusting to darkness"
            }
        }
    }
}

// Organ abstract class with receptor logic
abstract class Organ(val name: String, val receptors: List<String>) {
    var state: String = "Normal"
    abstract fun respondTo(hormone: Hormone)
    fun status(): String = "$name is $state"
}

// Specific organs
class Lungs : Organ("Lungs", listOf("Adrenaline")) {
    override fun respondTo(hormone: Hormone) = hormone.affect(this)
}

class Stomach : Organ("Stomach", listOf("Adrenaline")) {
    override fun respondTo(hormone: Hormone) = hormone.affect(this)
}

class Heart : Organ("Heart", listOf("Adrenaline")) {
    override fun respondTo(hormone: Hormone) = hormone.affect(this)
}

class Brain : Organ("Brain", listOf("Dopamine", "Melatonin")) {
    override fun respondTo(hormone: Hormone) = hormone.affect(this)
}

class Eyes : Organ("Eyes", listOf("Melatonin")) {
    override fun respondTo(hormone: Hormone) = hormone.affect(this)
}

// NerveSystem with active hormone list, decay and feedback
class NerveSystem {
    private val organs = listOf(
        Lungs(),
        Stomach(),
        Heart(),
        Brain(),
        Eyes()
    )

    private val activeHormones = mutableListOf<Hormone>()
    var mood: String = "Neutral"

    fun releaseHormone(hormone: Hormone) {
        println("\nReleasing ${hormone.name} with concentration ${"%.2f".format(hormone.concentration)}")
        activeHormones.add(hormone)

        when (hormone.name) {
            "Adrenaline" -> mood = "Alert"
            "Dopamine" -> mood = "Happy"
            "Melatonin" -> mood = "Sleepy"
        }
    }

    fun tick() {
        println("\n--- Time passes ---")
        activeHormones.removeIf { !it.decay() }

        organs.forEach { organ ->
            organ.state = "Normal" // reset before reaction
            activeHormones.forEach { hormone ->
                organ.respondTo(hormone)
            }
        }
    }

    fun report() {
        println("Mood: $mood")
        println("Active Hormones:")
        if (activeHormones.isEmpty()) println("  None")
        activeHormones.forEach {
            println("  ${it.name} - concentration: ${"%.2f".format(it.concentration)}")
        }
        organs.forEach { println(it.status()) }
    }
}

// Main simulation
fun main() {
    val system = NerveSystem()

    system.report()

    system.releaseHormone(Adrenaline(1.2))
    system.tick()
    system.report()

    system.tick()
    system.report()

    system.releaseHormone(Dopamine())
    system.tick()
    system.report()

    system.releaseHormone(Melatonin(0.8))
    system.tick()
    system.report()
}
